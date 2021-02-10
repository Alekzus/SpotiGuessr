package com.alexandre.marcq.spotiguessr.game

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandre.marcq.spotiguessr.api.PlaylistsApi
import com.alexandre.marcq.spotiguessr.api.PlaylistsApiStatus
import com.alexandre.marcq.spotiguessr.api.objects.playlist.Playlist
import com.alexandre.marcq.spotiguessr.api.objects.track.Track
import com.alexandre.marcq.spotiguessr.utils.Constants
import com.alexandre.marcq.spotiguessr.utils.GameDifficulty
import com.alexandre.marcq.spotiguessr.utils.GameScore
import com.alexandre.marcq.spotiguessr.utils.GameScoreStatus
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.round
import kotlin.properties.Delegates

class GameViewModel(
    private val accessToken: String,
    val playlist: Playlist,
    val appRemote: SpotifyAppRemote
) : ViewModel() {

    private val playerApi = appRemote.playerApi
    private var tracks = ArrayList<Track>()
    private var score = 0
    private var userGuessedName = false
    private var userGuessedArtist = false
    private var userSkipped = true

    var numberOfRounds = 0
    var tracksOnly = false
    var difficulty = GameDifficulty.EASY

    private val _currentTrackProgress = MutableLiveData(1)
    val currentTrackProgress: LiveData<Int> = _currentTrackProgress

    lateinit var currentTrack: Track
    lateinit var roundTimer: CountDownTimer
    private lateinit var sanitizedName: String
    private lateinit var sanitizedArtist: String

    private val _gameFinished = MutableLiveData<Int>()
    val gameFinished: LiveData<Int> = _gameFinished

    private val _roundStarting = MutableLiveData<Boolean>()
    val roundStarting: LiveData<Boolean> = _roundStarting

    private val _status = MutableLiveData<PlaylistsApiStatus>()
    val status: LiveData<PlaylistsApiStatus> = _status

    private val _beforeTick = MutableLiveData<Long>()
    val beforeTick: LiveData<Long> = _beforeTick

    private val _beforeDone = MutableLiveData<Boolean>()
    val beforeDone: LiveData<Boolean> = _beforeDone

    private val _userGuessed = MutableLiveData(GameScoreStatus.NONE)
    val userGuessed: LiveData<GameScoreStatus> = _userGuessed

    private val _userWon = MutableLiveData(false)
    val userWon: LiveData<Boolean> = _userWon

    private val _userFailed = MutableLiveData(false)
    val userFailed: LiveData<Boolean> = _userFailed

    private val _counterTick = MutableLiveData<Long>()
    val counterTick: LiveData<Long> = _counterTick

    private val _counterDone = MutableLiveData<Boolean>()
    val counterDone: LiveData<Boolean> = _counterDone

    init {
        getTracksUris()
    }

    val beforeRoundTimer = object : CountDownTimer(Constants.TIME_TO_PLAY, Constants.SECOND) {
        override fun onTick(millisUntilFinished: Long) {
            _beforeTick.value = (millisUntilFinished / 1000 + 1)
        }

        override fun onFinish() {
            _beforeDone.value = true
            if (tracksOnly) {
                _userGuessed.value = GameScoreStatus.ARTIST
                userGuessedArtist = true
                score += 1
            }
            playerApi.play(
                "spotify:track:${tracks[_currentTrackProgress.value?.minus(1)!!].id}"
            )
        }
    }

    private fun getRoundTime(): Long {
        return when (difficulty) {
            GameDifficulty.EASY -> Constants.TIME_EASY
            GameDifficulty.MEDIUM -> Constants.TIME_MEDIUM
            GameDifficulty.HARD -> Constants.TIME_HARD
        }
    }

    fun initRoundTimer() {
        roundTimer = object : CountDownTimer(getRoundTime(), Constants.SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _counterTick.value = (millisUntilFinished / 1000 + 1)
            }

            override fun onFinish() {
                _userFailed.value = true
            }

        }
    }

    fun skipTrack() {
        if (userSkipped) {
            _userFailed.value = true
            userSkipped = false
        } else {
            userGuessedName = false
            userGuessedArtist = false
            nextTrack()
        }
    }

    private fun nextTrack() {
        playerApi.pause()
        if (_currentTrackProgress.value == numberOfRounds) {
            _gameFinished.value = getScore()
            _roundStarting.value = false
        } else {
            _currentTrackProgress.value = _currentTrackProgress.value?.plus(1)
            currentTrack = tracks[_currentTrackProgress.value?.minus(1)!!]
            sanitizedName = GameScore.sanitizeInput(currentTrack.name)
            sanitizedArtist = GameScore.sanitizeInput(currentTrack.artists[0].name)
            _roundStarting.value = true
        }
    }

    private fun getScore(): Int? {
        return round(((score / (numberOfRounds.toFloat() * 2)) * 100)).toInt()
    }

    private fun getTracksUris() {
        viewModelScope.launch {
            val getTracksDeferred =
                PlaylistsApi.retrofitService.getPlaylistTracksAsync(accessToken, playlist.id)
            try {
                _status.value = PlaylistsApiStatus.LOADING
                val playlistTracks = getTracksDeferred.await().playlistTracks.shuffled()
                for (i in 0..numberOfRounds) {
                    tracks.add(playlistTracks[i].track)
                }
                currentTrack = tracks[_currentTrackProgress.value?.minus(1)!!]
                sanitizedName = GameScore.sanitizeInput(currentTrack.name)
                // TODO: 28/08/2020 Handle artists instead of using artists[0]
                sanitizedArtist = GameScore.sanitizeInput(currentTrack.artists[0].name)
                _status.value = PlaylistsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = PlaylistsApiStatus.ERROR
                tracks = ArrayList()
            }
        }
    }

    fun onGameFinished() {
        _gameFinished.value = -1
        score = 0
        _currentTrackProgress.value = 1
    }

    fun checkInput(input: CharSequence?) {
        val sanitizedInput = GameScore.sanitizeInput(input.toString())
        // TODO: 28/08/2020 Check score rules instead of just incrementing
        if (GameScore.doesMatch(sanitizedInput, sanitizedName)) {
            _userGuessed.value = GameScoreStatus.NAME
            userGuessedName = true
            score += 1
        } else if (GameScore.doesMatch(sanitizedInput, sanitizedArtist)) {
            _userGuessed.value = GameScoreStatus.ARTIST
            userGuessedArtist = true
            score += 1
        }

        if (userGuessedName && userGuessedArtist) {
            userSkipped = false
            _userWon.value = true
        }
    }

    fun onRoundStarted() {
        userSkipped = true
        _roundStarting.value = false
    }

    fun doneLoadingPlaylists() {
        _status.value = PlaylistsApiStatus.NONE
    }

    fun onUserGuessed() {
        _userGuessed.value = GameScoreStatus.NONE
    }

    fun onUserWon() {
        _userWon.value = false
        _userFailed.value = false
        userGuessedName = false
        userGuessedArtist = false
        roundTimer.cancel()
        appRemote.playerApi.pause()
    }

    fun startRoundTimer() {
        viewModelScope.launch {
            delay(Constants.SECOND)
            roundTimer.start()
        }
    }
}

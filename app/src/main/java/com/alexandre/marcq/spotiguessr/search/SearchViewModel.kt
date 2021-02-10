package com.alexandre.marcq.spotiguessr.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandre.marcq.spotiguessr.api.PlaylistsApi
import com.alexandre.marcq.spotiguessr.api.PlaylistsApiStatus
import com.alexandre.marcq.spotiguessr.api.objects.playlist.Playlist
import kotlinx.coroutines.launch

class SearchViewModel(private val accessToken: String) : ViewModel() {

    private val _status = MutableLiveData(PlaylistsApiStatus.NONE)
    val status: LiveData<PlaylistsApiStatus> = _status

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

    private val _networkError = MutableLiveData<String>()
    val networkError: LiveData<String> = _networkError

    private val _navigateToConfig = MutableLiveData<Playlist>()
    val navigateToConfig: LiveData<Playlist> = _navigateToConfig

    fun getPlaylists(query: String) {
        viewModelScope.launch {
            val getPlaylistsDeferred =
                PlaylistsApi.retrofitService.getPlaylistsSearchAsync(accessToken, query, "playlist")
            try {
                _status.value = PlaylistsApiStatus.LOADING
                val result = getPlaylistsDeferred.await()
                _status.value = PlaylistsApiStatus.DONE
                _playlists.value = result.playlists
            } catch (e: Exception) {
                _status.value = PlaylistsApiStatus.ERROR
                _playlists.value = ArrayList()
                _networkError.value = e.message
            }
        }
    }

    fun networkRetry() {
        _networkError.value = ""
    }

    fun navigateToConfig(playlist: Playlist) {
        _navigateToConfig.value = playlist
    }

    fun onNavigatedToConfig() {
        _navigateToConfig.value = null
    }
}
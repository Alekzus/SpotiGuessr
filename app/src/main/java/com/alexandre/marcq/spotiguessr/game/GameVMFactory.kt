package com.alexandre.marcq.spotiguessr.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexandre.marcq.spotiguessr.api.objects.playlist.Playlist
import com.spotify.android.appremote.api.SpotifyAppRemote

@Suppress("UNCHECKED_CAST")
class GameVMFactory constructor(
    private val accessToken: String,
    private val playlist: Playlist,
    private val appRemote: SpotifyAppRemote
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        with(modelClass) {
            if (isAssignableFrom(GameViewModel::class.java)) {
                return GameViewModel(accessToken, playlist, appRemote) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
}
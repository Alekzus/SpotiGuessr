package com.alexandre.marcq.spotiguessr.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexandre.marcq.spotiguessr.api.objects.playlist.Playlist
import com.alexandre.marcq.spotiguessr.game.GameViewModel

@Suppress("UNCHECKED_CAST")
class ConfigVMFactory constructor(
    private val playlist: Playlist
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        with(modelClass) {
            if (isAssignableFrom(ConfigViewModel::class.java)) {
                return ConfigViewModel(playlist) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
}
package com.alexandre.marcq.spotiguessr.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexandre.marcq.spotiguessr.playlists.PlaylistsViewModel
import com.alexandre.marcq.spotiguessr.search.SearchViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val accessToken: String
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(PlaylistsViewModel::class.java) -> PlaylistsViewModel(
                accessToken
            ) as T
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(
                accessToken
            ) as T
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
}
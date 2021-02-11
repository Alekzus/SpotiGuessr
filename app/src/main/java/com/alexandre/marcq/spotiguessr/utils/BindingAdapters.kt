package com.alexandre.marcq.spotiguessr.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexandre.marcq.spotiguessr.R
import com.alexandre.marcq.spotiguessr.api.PlaylistsApiStatus
import com.alexandre.marcq.spotiguessr.api.objects.image.Image
import com.alexandre.marcq.spotiguessr.api.objects.playlist.Playlist
import com.alexandre.marcq.spotiguessr.playlists.PlaylistAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Playlist>?) {
    val adapter = recyclerView.adapter as PlaylistAdapter
    adapter.submitList(data)
}

@BindingAdapter("tracksNumber")
fun bindTracksNumber(numberView: TextView, number: Number?) {
    // If the value is null, use "?"
    val numText = number ?: "?"
    numberView.text = numberView.context.getString(
        R.string.number_of_tracks,
        numText
    )
}

@BindingAdapter("playlistImage")
fun bindPlaylistImage(playlistImageView: ImageView, images: List<Image?>) {
    images[0]?.url?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(playlistImageView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    // If there is no picture provided
                    .placeholder(R.drawable.loading_animation)
                    // If the picture cannot be loaded
                    .error(R.drawable.ic_broken_image)
            )
            .into(playlistImageView)
    }
}

@BindingAdapter("playlistsApiStatus")
fun bindStatus(statusImageView: ImageView, status: PlaylistsApiStatus) {
    when (status) {
        PlaylistsApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        PlaylistsApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        else -> statusImageView.visibility = View.GONE
    }
}

@BindingAdapter("trackProgress", "trackTotal")
fun bindTrackProgress(trackCountView: TextView, progress: Int, total: Int) {
    trackCountView.text = trackCountView.context.getString(
        R.string.track_progress,
        progress,
        total
    )
}

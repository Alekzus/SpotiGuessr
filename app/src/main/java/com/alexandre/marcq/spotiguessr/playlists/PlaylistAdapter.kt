package com.alexandre.marcq.spotiguessr.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexandre.marcq.spotiguessr.api.objects.playlist.Playlist
import com.alexandre.marcq.spotiguessr.databinding.ItemRecyclerViewPlaylistBinding

class PlaylistAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Playlist, PlaylistAdapter.PlaylistViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(
            ItemRecyclerViewPlaylistBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = getItem(position)
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(playlist)
        }
    }

    // Object used to optimize the rendering of RecyclerView
    // Both functions are not exactly the same
    companion object DiffCallback : DiffUtil.ItemCallback<Playlist>() {

        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class PlaylistViewHolder(
        private var binding: ItemRecyclerViewPlaylistBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist) {
            binding.playlist = playlist
            binding.executePendingBindings()
        }
    }

    // Used to have a custom OnClickListener in the Fragment
    // The parameter is a function which will be described when the adapter is created elsewhere
    class OnClickListener(val clickListener: (playlist: Playlist) -> Unit) {
        // The onClick is equal to the function given above with playlist as a parameter ("it" elsewhere)
        fun onClick(playlist: Playlist) = clickListener(playlist)
    }
}
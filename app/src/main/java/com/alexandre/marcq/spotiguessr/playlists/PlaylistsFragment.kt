package com.alexandre.marcq.spotiguessr.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alexandre.marcq.spotiguessr.MainActivity
import com.alexandre.marcq.spotiguessr.databinding.FragmentPlaylistsBinding
import com.alexandre.marcq.spotiguessr.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class PlaylistsFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistsBinding

    private val _viewModel by viewModels<PlaylistsViewModel> {
        ViewModelFactory((activity as MainActivity).getToken())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(layoutInflater)

        binding.apply {
            lifecycleOwner = this@PlaylistsFragment

            viewModel = _viewModel

            playlistRecyclerView.adapter =
                PlaylistAdapter(PlaylistAdapter.OnClickListener {
                    _viewModel.navigateToConfig(it)
                })
        }

        _viewModel.navigateToConfig.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(
                    PlaylistsFragmentDirections.actionPlaylistsFragmentToConfigFragment(
                        it
                    )
                )
                _viewModel.onNavigatedToConfig()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _viewModel.networkError.observe(viewLifecycleOwner) {
            handleNetworkError(it, view)
        }
    }

    private fun handleNetworkError(error: String, view: View) {
        if (error.isNotEmpty()) {
            binding.playlistErrorText.text = error
            binding.playlistErrorText.visibility = View.VISIBLE
            Snackbar.make(view, "Error loading the playlists.", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry") {
                    _viewModel.getPlaylists()
                    _viewModel.networkRetry()
                }
                .show()
        } else {
            binding.playlistErrorText.visibility = View.GONE
        }
    }
}
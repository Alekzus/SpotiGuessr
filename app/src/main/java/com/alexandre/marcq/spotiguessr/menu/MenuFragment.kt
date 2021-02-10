package com.alexandre.marcq.spotiguessr.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.alexandre.marcq.spotiguessr.R
import com.alexandre.marcq.spotiguessr.databinding.FragmentMenuBinding

// TODO: 01/09/2020 Add a search functionality

class MenuFragment : Fragment() {

    private lateinit var viewModel: MenuViewModel
    private lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(layoutInflater)

        binding.apply {
            lifecycleOwner = this@MenuFragment

            myPlaylistsButton.setOnClickListener {
                findNavController().navigate(R.id.action_menuFragment_to_playlistsFragment)
            }

            searchButton.setOnClickListener {
                //findNavController().navigate(R.id.action_menuFragment_to_searchFragment)
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)
        binding.viewModel = viewModel
    }

}
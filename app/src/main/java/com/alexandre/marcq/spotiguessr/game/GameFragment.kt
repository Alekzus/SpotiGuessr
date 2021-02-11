package com.alexandre.marcq.spotiguessr.game

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alexandre.marcq.spotiguessr.MainActivity
import com.alexandre.marcq.spotiguessr.R
import com.alexandre.marcq.spotiguessr.api.PlaylistsApiStatus
import com.alexandre.marcq.spotiguessr.databinding.FragmentGameBinding
import com.alexandre.marcq.spotiguessr.utils.GameScoreStatus
import com.alexandre.marcq.spotiguessr.utils.goBack
import com.alexandre.marcq.spotiguessr.utils.hideKeyboard
import com.alexandre.marcq.spotiguessr.utils.showKeyboard

class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding

    // Different dialog popups
    private lateinit var readyDialog: AlertDialog
    private lateinit var loadingDialog: AlertDialog
    private lateinit var errorDialog: AlertDialog

    private val viewModel by viewModels<GameViewModel> {
        GameVMFactory(
            (activity as MainActivity).getToken(),
            GameFragmentArgs.fromBundle(requireArguments()).playlist,
            (activity as MainActivity).spotifyAppRemote
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        // Getting the arguments from the ConfigFragment
        with(GameFragmentArgs.fromBundle(requireArguments())) {
            viewModel.numberOfRounds = length
            viewModel.tracksOnly = tracksOnly
            viewModel.difficulty = difficulty
        }

        viewModel.initRoundTimer()

        binding.gameAnswerEdit.doOnTextChanged { text, _, _, _ ->
            viewModel.checkInput(text)
        }

        viewModel.roundStarting.observe(viewLifecycleOwner) {
            if (it) {
                resetUi()
                startTrackRound()
                viewModel.onRoundStarted()
            }
        }

        viewModel.userGuessed.observe(viewLifecycleOwner) {
            when (it) {
                GameScoreStatus.NAME
                -> {
                    binding.gameTrackName.text = viewModel.currentTrack.name
                    binding.gameAnswerEdit.text.clear()
                }
                // TODO: 28/08/2020 Same as the ViewModel, check for artists
                GameScoreStatus.ARTIST
                -> {
                    binding.gameTrackArtist.text = viewModel.currentTrack.artists[0].name
                    binding.gameAnswerEdit.text.clear()
                }
                else -> return@observe
            }
            viewModel.onUserGuessed()
        }

        viewModel.userWon.observe(viewLifecycleOwner) {
            if (it) {
                binding.gameSkipButton.text = getString(R.string.next)
                binding.gameAnswerEdit.hideKeyboard()
                binding.gameAnswerEdit.isEnabled = false
                viewModel.onUserWon()
            }
        }

        viewModel.userFailed.observe(viewLifecycleOwner) {
            if (it) {
                giveAnswer()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                PlaylistsApiStatus.LOADING ->
                    loadingDialog.show()
                PlaylistsApiStatus.DONE -> {
                    loadingDialog.dismiss()
                    readyDialog.show()
                }
                PlaylistsApiStatus.ERROR -> {
                    loadingDialog.dismiss()
                    errorDialog.show()
                }
                else -> return@observe
            }
            viewModel.doneLoadingPlaylists()
        }

        viewModel.gameFinished.observe(viewLifecycleOwner) {
            if (it != -1) {
                binding.gameSkipButton.isEnabled = false
                findNavController().navigate(
                    GameFragmentDirections.actionGameFragmentToScoreFragment(
                        it
                    )
                )
                viewModel.onGameFinished()
            }
        }

        viewModel.beforeTick.observe(viewLifecycleOwner) {
            if (it != 0L) {
                binding.gameMessage.text = it.toString()
            }
        }

        viewModel.beforeDone.observe(viewLifecycleOwner) {
            if (it) {
                binding.gameMessage.text = getString(R.string.good_luck)
                binding.gameSkipButton.isEnabled = true
                binding.gameAnswerEdit.isEnabled = true
                binding.gameAnswerEdit.showKeyboard()
                viewModel.startRoundTimer()
            }
        }

        viewModel.counterTick.observe(viewLifecycleOwner) {
            if (it != 0L) {
                binding.gameMessage.text = it.toString()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        readyDialog = AlertDialog.Builder(activity)
            .setMessage(R.string.are_you_ready)
            .setPositiveButton(
                R.string.lets_go
            ) { _, _ ->
                startTrackRound()
            }
            .setNegativeButton(
                R.string.go_back
            ) { _, _ ->
                goBack()
            }.create()

        loadingDialog = AlertDialog.Builder(activity)
            .setView(R.layout.dialog_loading)
            .setTitle(R.string.loading_tracks)
            .setNegativeButton(
                R.string.cancel
            ) { _, _ ->
                goBack()
            }.create()

        errorDialog = AlertDialog.Builder(activity)
            .setTitle(R.string.error)
            .setMessage(R.string.error_loading_tracks)
            .setPositiveButton(
                R.string.okay
            ) { _, _ ->
                goBack()
            }.create()
    }

    override fun onStop() {
        super.onStop()
        viewModel.beforeRoundTimer.cancel()
        viewModel.roundTimer.cancel()
        viewModel.appRemote.playerApi.pause()
    }

    private fun startTrackRound() {
        binding.gameSkipButton.isEnabled = false
        binding.gameAnswerEdit.isEnabled = false
        viewModel.beforeRoundTimer.start()
    }

    private fun resetUi() {
        binding.gameAnswerEdit.text.clear()
        binding.gameSkipButton.text = getString(R.string.skip)
        binding.gameTrackName.text = getString(R.string.track_name)
        binding.gameTrackArtist.text = getString(R.string.artist)
    }

    private fun giveAnswer() {
        if (binding.gameTrackName.hint.toString() == getString(R.string.track_name)) {
            binding.gameTrackName.text = viewModel.currentTrack.name
        }

        if (binding.gameTrackArtist.hint == getString(R.string.artist)) {
            binding.gameTrackArtist.text = viewModel.currentTrack.artists[0].name
        }
        viewModel.roundTimer.cancel()
        binding.gameMessage.text = getString(R.string.round_over)
        binding.gameAnswerEdit.isEnabled = false
        binding.gameSkipButton.text = getString(R.string.next)
    }
}
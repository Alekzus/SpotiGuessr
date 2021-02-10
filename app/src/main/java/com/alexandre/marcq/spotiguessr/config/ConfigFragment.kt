package com.alexandre.marcq.spotiguessr.config

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alexandre.marcq.spotiguessr.R
import com.alexandre.marcq.spotiguessr.databinding.FragmentConfigBinding
import com.alexandre.marcq.spotiguessr.utils.GameDifficulty
import com.alexandre.marcq.spotiguessr.utils.goBack

class ConfigFragment : Fragment() {

    private val _viewModel by viewModels<ConfigViewModel> {
        ConfigVMFactory(
            ConfigFragmentArgs.fromBundle(requireArguments()).playlist
        )
    }

    private lateinit var binding: FragmentConfigBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentConfigBinding.inflate(layoutInflater)

        binding.apply {
            lifecycleOwner = this@ConfigFragment

            viewModel = _viewModel

            configCancelButton.setOnClickListener {
                goBack()
            }

            configDoneButton.isEnabled = false
            configDoneButton.setOnClickListener {
                findNavController().navigate(
                    ConfigFragmentDirections.actionConfigFragmentToGameFragment(
                        binding.configOnlytracksSwitch.isChecked,
                        _viewModel.playlist,
                        getDifficulty(),
                        binding.configLengthEdit.text.toString().toInt()
                    )
                )
            }

            configLengthLimitText.text = getString(
                R.string.length_limit,
                _viewModel.playlist.playlistTrackNumber.total
            )

            configLengthEdit.doAfterTextChanged {
                _viewModel.checkInput(it)
            }
        }

        _viewModel.lengthIsWrong.observe(viewLifecycleOwner) {
            if (it) {
                binding.configLengthEdit.setTextColor(resources.getColor(R.color.secondaryLightColor))
                binding.configDoneButton.isEnabled = false
            } else {
                // FIXME: 05/09/2020 getColor is deprecated
                binding.configLengthEdit.setTextColor(resources.getColor(R.color.secondaryTextColor))
                binding.configDoneButton.isEnabled = true
            }
        }

        return binding.root
    }

    private fun getDifficulty(): GameDifficulty {
        return when (binding.configDifficultyGroup.checkedRadioButtonId) {
            R.id.config_easy_radio -> GameDifficulty.EASY
            R.id.config_medium_radio -> GameDifficulty.MEDIUM
            R.id.config_hard_radio -> GameDifficulty.HARD
            else -> throw IllegalStateException("Error while getting the difficulty.")
        }
    }

}
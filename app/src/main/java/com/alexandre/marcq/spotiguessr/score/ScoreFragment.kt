package com.alexandre.marcq.spotiguessr.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alexandre.marcq.spotiguessr.R
import com.alexandre.marcq.spotiguessr.databinding.FragmentScoreBinding
import com.alexandre.marcq.spotiguessr.utils.goBack
import kotlin.properties.Delegates

class ScoreFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(ScoreViewModel::class.java)
    }

    private var score by Delegates.notNull<Int>()
    private lateinit var binding: FragmentScoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        score = ScoreFragmentArgs.fromBundle(requireArguments()).score

        binding = FragmentScoreBinding.inflate(layoutInflater)

        binding.scoreText.text = getString(
            R.string.score_percentage,
            ScoreFragmentArgs.fromBundle(requireArguments()).score
        )

        binding.scoreShareButon.setOnClickListener {
            viewModel.shareScore(
                getString(
                    R.string.share_intent_message,
                    score
                )
            )
        }

        binding.scoreGoBackButton.setOnClickListener {
            goBack()
        }

        viewModel.shareScore.observe(viewLifecycleOwner) {
            if (it != null) {
                startActivity(it)
                viewModel.onScoreSent()
            }
        }

        return binding.root
    }
}
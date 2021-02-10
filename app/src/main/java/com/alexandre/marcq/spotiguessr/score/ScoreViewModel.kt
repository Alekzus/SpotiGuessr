package com.alexandre.marcq.spotiguessr.score

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel : ViewModel() {

    private val _shareScore = MutableLiveData<Intent>()
    val shareScore : LiveData<Intent> = _shareScore

    fun shareScore(message: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                message
            )
            type = "text/plain"
        }
        _shareScore.value = Intent.createChooser(sendIntent, null)
    }

    fun onScoreSent() {
        _shareScore.value = null
    }
}
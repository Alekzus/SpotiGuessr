package com.alexandre.marcq.spotiguessr

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alexandre.marcq.spotiguessr.utils.Constants.CLIENT_ID
import com.alexandre.marcq.spotiguessr.utils.Constants.REDIRECT_URI
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

class MainActivity : AppCompatActivity() {

    lateinit var spotifyAppRemote: SpotifyAppRemote

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(
            this,
            connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(app: SpotifyAppRemote) {
                    spotifyAppRemote = app
                }

                override fun onFailure(throwable: Throwable) {
                }

            })
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(spotifyAppRemote)
    }

    override fun onDestroy() {
        super.onDestroy()
        SpotifyAppRemote.disconnect(spotifyAppRemote)
    }

    fun getToken(): String {
        return "Bearer ${intent.getStringExtra("Token")}"
    }
}
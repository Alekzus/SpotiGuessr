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

        // Used to connect to Spotify
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        // Connecting to Spotify
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

    // Important to disconnect from Spotify when onStop and onDestroy
    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(spotifyAppRemote)
    }

    override fun onDestroy() {
        super.onDestroy()
        SpotifyAppRemote.disconnect(spotifyAppRemote)
    }

    /**
     * Gets user token.
     *
     * This function retrieves the token from Spotify.
     * Used in [GameFragment] and [PlaylistsFragment].
     *
     * @return the token in the form of "Bearer `<TOKEN`>".
     */
    fun getToken(): String {
        return "Bearer ${intent.getStringExtra("Token")}"
    }
}
package com.alexandre.marcq.spotiguessr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alexandre.marcq.spotiguessr.utils.Constants.CLIENT_ID
import com.alexandre.marcq.spotiguessr.utils.Constants.REDIRECT_URI
import com.alexandre.marcq.spotiguessr.utils.Constants.REQUEST_CODE
import com.google.android.material.snackbar.Snackbar
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    // Used to set the app's authorizations regarding Spotify
    // Here the app need to control Spotify and read both private and collaborative playlists
    private val request: AuthorizationRequest = AuthorizationRequest.Builder(
        CLIENT_ID,
        AuthorizationResponse.Type.TOKEN,
        REDIRECT_URI
    ).setScopes(arrayOf("app-remote-control", "playlist-read-private", "playlist-read-collaborative"))
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Opens Spotify to login
        login_button.setOnClickListener {
            AuthorizationClient.openLoginActivity(
                this,
                REQUEST_CODE,
                request
            )
        }
    }

    // TODO: 10/02/2021 Generate logs on errors to inform user to check logs
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // If the result is from the login intent and the data is not null:
        if (requestCode == REQUEST_CODE && data != null) {
            // Get the response
            val response = AuthorizationClient.getResponse(resultCode, data)
            when (response.type) {
                // If the response is a token:
                AuthorizationResponse.Type.TOKEN
                        -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("Token", response.accessToken)
                    // Launch the MainActivity with the token in the intent
                    startActivity(intent)
                }
                // Else if the response is an error:
                AuthorizationResponse.Type.ERROR
                        // Snackbar with the error
                        -> Snackbar.make(login_button, "Error getting the token ${response.error}", Snackbar.LENGTH_LONG).show()
                // Else the response is unknown, inform the user
                else -> Snackbar.make(login_button, "Unknown type : ${response.type}", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
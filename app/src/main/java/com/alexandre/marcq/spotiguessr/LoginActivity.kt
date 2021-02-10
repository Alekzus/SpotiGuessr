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

    private val request: AuthorizationRequest = AuthorizationRequest.Builder(
        CLIENT_ID,
        AuthorizationResponse.Type.TOKEN,
        REDIRECT_URI
    ).setScopes(arrayOf("app-remote-control", "playlist-read-private", "playlist-read-collaborative"))
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button.setOnClickListener {
            AuthorizationClient.openLoginActivity(
                this,
                REQUEST_CODE,
                request
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && data != null) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN
                        -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("Token", response.accessToken)
                    startActivity(intent)
                }
                AuthorizationResponse.Type.ERROR
                        -> Snackbar.make(login_button, "Error getting the token ${response.error}", Snackbar.LENGTH_LONG).show()
                else -> Snackbar.make(login_button, "Unknown type : ${response.type}", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
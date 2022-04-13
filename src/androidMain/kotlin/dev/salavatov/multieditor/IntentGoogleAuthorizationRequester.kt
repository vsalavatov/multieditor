package dev.salavatov.multieditor

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import dev.salavatov.multifs.cloud.googledrive.GoogleAppCredentials
import dev.salavatov.multifs.cloud.googledrive.GoogleAuthTokens
import dev.salavatov.multifs.cloud.googledrive.GoogleAuthorizationRequester

class IntentGoogleAuthorizationRequester(private val baseActivity: Activity, private val googleAppCredentials: GoogleAppCredentials) : GoogleAuthorizationRequester {
    override suspend fun requestAuthorization(): GoogleAuthTokens {
//        val startForResult =
//            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    val intent = result.data
//                    if (result.data != null) {
//                        val task: Task<GoogleSignInAccount> =
//                            GoogleSignIn.getSignedInAccountFromIntent(intent)
//                        handleSignInResult(task)
//                    }
//                }
//            }
//        val googleSignInClient = getGoogleLoginAuth("")
//        Button(
//            onClick = {
//                startForResult.launch(googleSignInClient.signInIntent)
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 16.dp, end = 16.dp),
//            shape = RoundedCornerShape(6.dp),
//            colors = ButtonDefaults.buttonColors(
//                backgroundColor = Color.Black,
//                contentColor = Color.White
//            )
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.common_google_signin_btn_icon_dark),
//                contentDescription = ""
//            )
//            Text(text = "Sign in with Google", modifier = Modifier.padding(6.dp))
//        }
        TODO("Not yet implemented")
    }

    override suspend fun refreshAuthorization(expired: GoogleAuthTokens): GoogleAuthTokens {
        TODO("Not yet implemented")
    }
}

package dev.salavatov.multieditor

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import dev.salavatov.multieditor.state.AppState
import dev.salavatov.multieditor.state.EditorState
import dev.salavatov.multieditor.state.NavigationState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val appState = remember {
                AppState(
                    EditorState(
                        mutableStateOf(null),
                        mutableStateOf(""),
                        mutableStateOf(false)
                    ),
                    NavigationState(
                        mutableStateOf(emptyList()),
                        mutableStateOf(makeStorageList(StorageConfig(this)))
                    )
                )
            }
//            AppUI(appState)
            LoginScreen()
        }
    }

    private fun getGoogleLoginAuth(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
            .requestIdToken("783177635948-u07uf59d2u1uttk21bt21gl3q6597vb2.apps.googleusercontent.com")
//            .requestIdToken("783177635948-m4m47c0v9igfk4vnua3feckbpghqrv17.apps.googleusercontent.com")
            .requestId()
//            .requestProfile()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    @Composable
    fun LoginScreen() {
        val scope = rememberCoroutineScope()
        val text = remember { mutableStateOf("none") }
        val startForResult =
            rememberLauncherForActivityResult(StartActivityForResult()) { result: ActivityResult ->
                Log.e("LOGLOG", "${result.data!!.extras!!.keySet().joinToString(" ")}")
                Log.e("LOGLOG", "${result.data!!.extras!!["googleSignInStatus"]}")
                Log.e("LOGLOG", "${result.data!!.extras!!["googleSignInAccount"]}")
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    if (result.data != null) {
                        val task: Task<GoogleSignInAccount> =
                            GoogleSignIn.getSignedInAccountFromIntent(intent)
                        assert(task.isComplete)
                        val data = task.result
                        text.value = "${data.id} ${data.idToken}"
                    }
                } else {
                    Log.e("LOGLOG", "HERE1 ${result.resultCode}")
                }
            }

        val googleSignInClient = getGoogleLoginAuth(LocalContext.current)
        Column {
            Button(
                onClick = {
                    startForResult.launch(googleSignInClient.signInIntent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Sign in with Google", modifier = Modifier.padding(6.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text.value)
        }
    }
}
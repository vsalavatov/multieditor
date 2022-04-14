package dev.salavatov.multieditor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import dev.salavatov.multieditor.state.AppState
import dev.salavatov.multieditor.state.EditorState
import dev.salavatov.multieditor.state.NavigationState
import dev.salavatov.multieditor.state.makeStartState
import dev.salavatov.multieditor.ui.AppUI
import dev.salavatov.multifs.cloud.googledrive.GoogleAppCredentials

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val googleAuth = makeGoogleAuthorizationRequester(
                GoogleAppCredentials(
                    "783177635948-u07uf59d2u1uttk21bt21gl3q6597vb2.apps.googleusercontent.com",
                    "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"
                )
            )
            val storages = remember { Storages(googleAuth) }
            val appState = makeStartState(storages.asList())
            AppUI(appState)
        }
    }
}
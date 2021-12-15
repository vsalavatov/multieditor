package dev.salavatov.multieditor

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.salavatov.multifs.cloud.googledrive.GoogleAppCredentials
import dev.salavatov.multifs.cloud.googledrive.sampleGoogleAuthenticator
import kotlinx.coroutines.launch

@Composable
fun App() {
    val coroutineScope = rememberCoroutineScope()
    var tokens by remember { mutableStateOf("") }
    val gauth = sampleGoogleAuthenticator(
        GoogleAppCredentials(
            "783177635948-ishda9322n9pk96b2uc6opp729ia0a42.apps.googleusercontent.com",
            "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"
        )
    )

    MaterialTheme {
        Column {
            Button(onClick = {
                coroutineScope.launch {
                    tokens = gauth.authenticate().toString()
                }
            }) {
                Text("Login")
            }
            Text(tokens)
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
package dev.salavatov.multieditor

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dev.salavatov.multifs.cloud.googledrive.GoogleAppCredentials
import dev.salavatov.multifs.cloud.googledrive.IntentGoogleAuthorizationRequester
import kotlinx.coroutines.CompletableDeferred

@Composable
fun makeGoogleAuthorizationRequester(googleAppCredentials: GoogleAppCredentials): IntentGoogleAuthorizationRequester {
    var resultFuture = remember { CompletableDeferred<ActivityResult>() }
    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            resultFuture.complete(it)
        }
    return IntentGoogleAuthorizationRequester(
        googleAppCredentials,
        LocalContext.current,
    ) { intent ->
        resultFuture = CompletableDeferred<ActivityResult>()
        startForResult.launch(intent)
        resultFuture.await()
    }
}

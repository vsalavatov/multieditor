package dev.salavatov.multieditor

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dev.salavatov.multifs.cloud.googledrive.*
import dev.salavatov.multifs.vfs.GenericFS


actual class StorageConfig(val activity: Activity)

actual fun makeStorageList(config: StorageConfig): List<NamedStorageFactory> {
//    val gdfs = NamedStorageFactory("Google.Drive") {
//        val googleAuth =
//            IntentGoogleAuthorizationRequester(
//                GoogleAppCredentials(
//                    "783177635948-u07uf59d2u1uttk21bt21gl3q6597vb2.apps.googleusercontent.com",
//                    "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"
//                )
//            )
//        val gapi = GoogleDriveAPI(googleAuth)
//        GoogleDriveFS(gapi)
//    }
//    return listOf(gdfs)
    return emptyList()
}
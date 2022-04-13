package dev.salavatov.multieditor

import dev.salavatov.multifs.cloud.googledrive.GoogleAppCredentials
import dev.salavatov.multifs.cloud.googledrive.GoogleDriveAPI
import dev.salavatov.multifs.cloud.googledrive.GoogleDriveFS
import dev.salavatov.multifs.cloud.googledrive.PopupGoogleAuthorizationRequester
import dev.salavatov.multifs.vfs.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.job
import kotlin.coroutines.coroutineContext

actual class StorageConfig

actual fun makeStorageList(config: StorageConfig): List<NamedStorageFactory> {
    val gdfs = NamedStorageFactory("Google.Drive") {
        val googleAuth =
            PopupGoogleAuthorizationRequester(
                GoogleAppCredentials(
                    "783177635948-u07uf59d2u1uttk21bt21gl3q6597vb2.apps.googleusercontent.com",
                    "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"
                )
            )
        val gapi = GoogleDriveAPI(googleAuth)
        GoogleDriveFS(gapi)
    }
    return listOf(gdfs)
}
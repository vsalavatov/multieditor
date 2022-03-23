package dev.salavatov.multieditor

import dev.salavatov.multifs.cloud.googledrive.GoogleAppCredentials
import dev.salavatov.multifs.cloud.googledrive.GoogleDriveAPI
import dev.salavatov.multifs.cloud.googledrive.GoogleDriveFS
import dev.salavatov.multifs.cloud.googledrive.PopupGoogleAuthorizationRequester
import dev.salavatov.multifs.vfs.*

actual object StorageBackends {
    val gdfs = StorageFactory("Google.Drive") {
        val googleAuth =
            PopupGoogleAuthorizationRequester(
                GoogleAppCredentials(
                    "783177635948-ishda9322n9pk96b2uc6opp729ia0a42.apps.googleusercontent.com",
                    "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"
                )
            )
        val gapi = GoogleDriveAPI(googleAuth)
        GoogleDriveFS(gapi)
    }

    actual val backends: List<StorageFactory>
        get() = listOf(gdfs)
}
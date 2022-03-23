package dev.salavatov.multieditor

import dev.salavatov.multieditor.multifs.CacheGoogleAuthorizationRequester
import dev.salavatov.multifs.cloud.googledrive.*
import dev.salavatov.multifs.systemfs.SystemFS
import dev.salavatov.multifs.vfs.GenericFS

actual object StorageBackends {
    val systemfs = StorageFactory("Local device") { SystemFS() }
    val gdfs = StorageFactory("Google.Drive") {
        val googleAuth = CacheGoogleAuthorizationRequester(
            CallbackGoogleAuthorizationRequester(
                GoogleAppCredentials(
                    "783177635948-ishda9322n9pk96b2uc6opp729ia0a42.apps.googleusercontent.com",
                    "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"
                )
            )
        )
        val gapi = GoogleDriveAPI(googleAuth)
        GoogleDriveFS(gapi)
    }

    actual val backends: List<StorageFactory> = listOf(systemfs, gdfs)
}
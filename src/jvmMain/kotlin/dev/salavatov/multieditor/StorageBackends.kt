package dev.salavatov.multieditor

import dev.salavatov.multieditor.multifs.CacheGoogleAuthorizationRequester
import dev.salavatov.multifs.cloud.googledrive.*
import dev.salavatov.multifs.systemfs.SystemFS
import dev.salavatov.multifs.vfs.GenericFS

object StorageBackends {
    data class Storage(val name: String, val init: suspend () -> GenericFS)

    val systemfs = Storage("Local device") { SystemFS() }
    val gdfs = Storage("Google.Drive") {
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

    val backends: List<Storage> = listOf(systemfs, gdfs)
}
package dev.salavatov.multieditor

import dev.salavatov.multieditor.multifs.CacheGoogleAuthorizationRequester
import dev.salavatov.multifs.cloud.googledrive.*
import dev.salavatov.multifs.systemfs.SystemFS
import dev.salavatov.multifs.vfs.GenericFS


fun makeStorageList(): List<NamedStorageFactory> {
    val systemfs = NamedStorageFactory("Local device") { SystemFS() }
    val gdfs = NamedStorageFactory("Google.Drive") {
        val googleAuth = CacheGoogleAuthorizationRequester(
            HttpCallbackGoogleAuthorizationRequester(
                GoogleAppCredentials(
                    "783177635948-ishda9322n9pk96b2uc6opp729ia0a42.apps.googleusercontent.com",
                    "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"
                )
            )
        )
        val gapi = GoogleDriveAPI(googleAuth)
        GoogleDriveFS(gapi)
    }
    return listOf(systemfs, gdfs)
}
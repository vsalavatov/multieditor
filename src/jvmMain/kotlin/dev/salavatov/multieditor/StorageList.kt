package dev.salavatov.multieditor

import dev.salavatov.multieditor.multifs.CacheGoogleAuthorizationRequester
import dev.salavatov.multifs.cloud.googledrive.GoogleAppCredentials
import dev.salavatov.multifs.cloud.googledrive.GoogleDriveAPI
import dev.salavatov.multifs.cloud.googledrive.GoogleDriveFS
import dev.salavatov.multifs.cloud.googledrive.HttpCallbackGoogleAuthorizationRequester
import dev.salavatov.multifs.systemfs.SystemFS


fun makeStorageList(): List<NamedStorageFactory> {
    val systemfs = NamedStorageFactory("Local device") { SystemFS() }
    val gdfs = NamedStorageFactory("Google Drive") {
        val googleAuth = CacheGoogleAuthorizationRequester(
            HttpCallbackGoogleAuthorizationRequester(
                GoogleAppCredentials(
                    "783177635948-ishda9322n9pk96b2uc6opp729ia0a42.apps.googleusercontent.com",
                    "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"
                ),
                GoogleDriveAPI.Companion.DriveScope.General
            )
        )
        val gapi = GoogleDriveAPI(googleAuth)

        GoogleDriveFS(gapi).also {
            it.root.listFolder() // trigger auth
        }
    }
    return listOf(systemfs, gdfs)
}
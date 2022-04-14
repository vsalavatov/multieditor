package dev.salavatov.multieditor

import dev.salavatov.multifs.cloud.googledrive.GoogleDriveAPI
import dev.salavatov.multifs.cloud.googledrive.GoogleDriveFS
import dev.salavatov.multifs.cloud.googledrive.IntentGoogleAuthorizationRequester


class Storages(
    private val googleAuth: IntentGoogleAuthorizationRequester
) {
    private val googleDrive = NamedStorageFactory("Google Drive") {
        val gapi = GoogleDriveAPI(googleAuth)
        GoogleDriveFS(gapi)
    }

    fun asList(): List<NamedStorageFactory> {
        return listOf(googleDrive)
    }
}
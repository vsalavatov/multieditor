package dev.salavatov.multieditor

import dev.salavatov.multifs.cloud.googledrive.GoogleDriveAPI
import dev.salavatov.multifs.cloud.googledrive.GoogleDriveFS
import dev.salavatov.multifs.cloud.googledrive.PopupGoogleAuthorizationRequester

fun makeStorageList(): List<NamedStorageFactory> {
    val gdfs = NamedStorageFactory("Google.Drive") {
        val googleAuth =
            PopupGoogleAuthorizationRequester(
//                GoogleAppCredentials(
//                    "783177635948-u07uf59d2u1uttk21bt21gl3q6597vb2.apps.googleusercontent.com",
//                    "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"
//                ),
                GoogleDriveAPI.Companion.DriveScope.General
            )
        val gapi = GoogleDriveAPI(googleAuth)
        GoogleDriveFS(gapi).also {
            it.root.listFolder() // trigger auth
        }
    }
    return listOf(gdfs)
}
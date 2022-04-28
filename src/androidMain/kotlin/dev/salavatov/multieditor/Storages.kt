package dev.salavatov.multieditor

import dev.salavatov.multifs.cloud.googledrive.GoogleDriveAPI
import dev.salavatov.multifs.cloud.googledrive.GoogleDriveFS
import dev.salavatov.multifs.cloud.googledrive.IntentGoogleAuthorizationRequester
import dev.salavatov.multifs.sqlite.SqliteFS
import dev.salavatov.multifs.sqlite.SqliteFSDatabaseHelper
import dev.salavatov.multifs.systemfs.SystemFS
import java.nio.file.Path


class Storages(
    private val googleAuth: IntentGoogleAuthorizationRequester,
    private val sqliteFsDbHelper: SqliteFSDatabaseHelper,
    private val systemFSRoot: Path
) {
    private val sqlite = NamedStorageFactory("SQLite") {
        SqliteFS(sqliteFsDbHelper)
    }

    private val googleDrive = NamedStorageFactory("Google Drive") {
        val gapi = GoogleDriveAPI(googleAuth)
        GoogleDriveFS(gapi).also {
            it.root.listFolder() // trigger auth
        }
    }

    private val systemFS = NamedStorageFactory("Local") {
        SystemFS(systemFSRoot)
    }

    fun asList(): List<NamedStorageFactory> {
        return listOf(
            googleDrive,
            sqlite,
            systemFS
        )
    }
}
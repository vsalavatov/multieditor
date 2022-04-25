package dev.salavatov.multieditor

import android.util.Log
import dev.salavatov.multifs.cloud.googledrive.GoogleDriveAPI
import dev.salavatov.multifs.cloud.googledrive.GoogleDriveFS
import dev.salavatov.multifs.cloud.googledrive.IntentGoogleAuthorizationRequester
import dev.salavatov.multifs.sqlite.SqliteFS
import dev.salavatov.multifs.sqlite.SqliteFSDatabaseHelper
import dev.salavatov.multifs.systemfs.SystemFS
import java.nio.file.Path
import kotlin.io.path.pathString


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
        GoogleDriveFS(gapi)
    }

    private val systemFS = NamedStorageFactory("Local") {
        SystemFS(systemFSRoot)
    }

    fun asList(): List<NamedStorageFactory> {
        return listOf(
//            googleDrive,
//            sqlite,
            systemFS
        )
    }
}
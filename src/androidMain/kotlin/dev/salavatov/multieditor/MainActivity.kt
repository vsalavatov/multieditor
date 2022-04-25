package dev.salavatov.multieditor

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dev.salavatov.multieditor.state.makeStartAppState
import dev.salavatov.multieditor.ui.AppUI
import dev.salavatov.multifs.cloud.googledrive.GoogleAppCredentials
import dev.salavatov.multifs.cloud.googledrive.GoogleDriveAPI
import dev.salavatov.multifs.sqlite.SqliteFSDatabaseHelper


class MainActivity : ComponentActivity() {
    private var permissionsGranted by mutableStateOf(false)
    private lateinit var sqliteDBHelper: SqliteFSDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkPermissions()) {
            permissionsGranted = true
        }

        sqliteDBHelper = SqliteFSDatabaseHelper(applicationContext, "fs.db")

        setContent {
            if (!permissionsGranted) {
                Text("waiting for permissions")
            } else {
                val googleAuth = makeGoogleAuthorizationRequester(
                    GoogleAppCredentials(
                        "783177635948-u07uf59d2u1uttk21bt21gl3q6597vb2.apps.googleusercontent.com",
                        "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"
                    ),
                    GoogleDriveAPI.Companion.DriveScope.General
                )
                val storages = remember {
                    Storages(
                        googleAuth,
                        sqliteDBHelper,
//                        applicationContext.filesDir.toPath(), // /data/user/0/dev.salavatov.multieditor/files
//                        application.externalCacheDir!!.toPath(), // /storage/emulated/0/Android/data/dev.salavatov.multieditor/cache
//                        application.externalMediaDirs[0].toPath(), // /storage/emulated/0/Android/media/dev.salavatov.multieditor
//                        Paths.get("/storage/self/primary"), // same as below
//                        Paths.get("/storage/emulated/0"), // same as below
//                        Paths.get("/sdcard"), // same as below
                        Environment.getExternalStorageDirectory().toPath()
                    )
                }
                val appState = makeStartAppState(storages.asList())
                AppUI(appState)
            }
        }
    }

    // https://stackoverflow.com/questions/33162152/storage-permission-error-in-marshmallow/41221852#41221852
    private fun checkPermissions(): Boolean {
        var result: Int
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(this, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), 100)
            return false
        }
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = true
            }
            return
        }
    }

    override fun onDestroy() {
        sqliteDBHelper.close()
        super.onDestroy()
    }

    private val permissions = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        // API >= 30: Manifest.permission.MANAGE_EXTERNAL_STORAGE
    )
}
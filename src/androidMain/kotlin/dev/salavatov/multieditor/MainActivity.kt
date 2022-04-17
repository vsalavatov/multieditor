package dev.salavatov.multieditor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import dev.salavatov.multieditor.state.makeStartAppState
import dev.salavatov.multieditor.ui.AppUI
import dev.salavatov.multifs.cloud.googledrive.GoogleAppCredentials
import dev.salavatov.multifs.sqlite.SqliteFSDatabaseHelper

class MainActivity : ComponentActivity() {

    private lateinit var sqliteDBHelper: SqliteFSDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sqliteDBHelper = SqliteFSDatabaseHelper(applicationContext, "fs.db")
        setContent {
            val googleAuth = makeGoogleAuthorizationRequester(
                GoogleAppCredentials(
                    "783177635948-u07uf59d2u1uttk21bt21gl3q6597vb2.apps.googleusercontent.com",
                    "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"
                )
            )
            val storages = remember { Storages(googleAuth, sqliteDBHelper) }
            val appState = makeStartAppState(storages.asList())
            AppUI(appState)
        }
    }

    override fun onDestroy() {
        sqliteDBHelper.close()
        super.onDestroy()
    }
}
package dev.salavatov.multieditor

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.salavatov.multieditor.state.makeStartAppState
import dev.salavatov.multieditor.ui.AppUI

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        val storages = remember { makeStorageList() }
        val appState = makeStartAppState(storages)
        AppUI(appState)
    }
}
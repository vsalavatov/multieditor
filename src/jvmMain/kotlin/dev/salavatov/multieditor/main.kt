package dev.salavatov.multieditor

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.salavatov.multieditor.ui.AppUI

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        AppUI()
    }
}
package dev.salavatov.multieditor.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import dev.salavatov.multifs.vfs.File

class EditorState(
    val fileState: MutableState<File?>,
    val contentState: MutableState<String>,
    val savingState: MutableState<Boolean>
) {
    var file by fileState
    var content by contentState
    var saving by savingState
}
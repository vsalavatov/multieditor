package dev.salavatov.multieditor.state

import androidx.compose.runtime.MutableState
import dev.salavatov.multifs.vfs.File

class EditorState(
    val file: MutableState<File?>,
    val content: MutableState<String>,
    val saving: MutableState<Boolean>
)
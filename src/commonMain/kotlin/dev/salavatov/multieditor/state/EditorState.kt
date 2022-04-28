package dev.salavatov.multieditor.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import dev.salavatov.multieditor.expect.MultifsDispatcher
import dev.salavatov.multifs.vfs.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class EditorState(
    val fileState: MutableState<File?>,
    val contentState: MutableState<String>,
    val savingState: MutableState<Boolean>
) {
    var file by fileState
    var content by contentState
    var saving by savingState

    fun CoroutineScope.launchSaveContent() {
        val file = fileState.value
        if (file != null && !saving) {
            saving = true
            launch(MultifsDispatcher()) {
                file.write(content.encodeToByteArray())
            }.invokeOnCompletion {
                saving = false
            }
        }
    }

    fun CoroutineScope.launchFileOpen(targetFile: File) {
        launch(MultifsDispatcher()) {
            val contentBytes = targetFile.read()
            if (!saving) {
                content = contentBytes.decodeToString()
                file = targetFile
            }
        }
    }
}
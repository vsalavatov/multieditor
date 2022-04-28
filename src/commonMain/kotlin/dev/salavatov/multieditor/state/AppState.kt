package dev.salavatov.multieditor.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import dev.salavatov.multieditor.NamedStorageFactory
import dev.salavatov.multieditor.Storage
import dev.salavatov.multieditor.expect.MultifsDispatcher
import dev.salavatov.multifs.vfs.File
import dev.salavatov.multifs.vfs.Folder
import dev.salavatov.multifs.vfs.VFSNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AppState(
    val editor: EditorState,
    val navigation: NavigationState,
    val errorState: MutableState<Throwable?>
) {

    fun CoroutineScope.launchSafe(block: suspend () -> Unit) =
        launch(MultifsDispatcher()) {
            try {
                block()
            } catch (e: Exception) {
                errorState.value = e
            }
        }


    fun CoroutineScope.launchSaveContent() {
        val file = editor.fileState.value
        if (file != null && !editor.saving) {
            editor.saving = true
            launchSafe {
                file.write(editor.content.encodeToByteArray())
            }.invokeOnCompletion {
                editor.saving = false
            }
        }
    }

    fun CoroutineScope.launchFileOpen(targetFile: File) =
        launchSafe {
            val contentBytes = targetFile.read()
            if (!editor.saving) {
                editor.content = contentBytes.decodeToString()
                editor.file = targetFile
            }
        }


    fun CoroutineScope.launchFolderList(folder: Folder, container: SnapshotStateList<VFSNode>) =
        launchSafe {
            val data = folder.listFolder()
            container.clear()
            container.addAll(data)
        }
}

@Composable
fun makeStartAppState(storages: List<NamedStorageFactory>): AppState {
    return remember {
        AppState(
            EditorState(
                mutableStateOf(null),
                mutableStateOf(""),
                mutableStateOf(false)
            ),
            NavigationState(
                mutableStateOf(storages)
            ),
            mutableStateOf(null)
        )
    }
}
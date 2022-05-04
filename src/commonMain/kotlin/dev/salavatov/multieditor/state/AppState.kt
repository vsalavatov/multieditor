package dev.salavatov.multieditor.state

import androidx.compose.runtime.*
import dev.salavatov.multieditor.NamedStorageFactory
import dev.salavatov.multieditor.expect.MultifsDispatcher
import dev.salavatov.multifs.vfs.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AppState(
    val editor: EditorState,
    val navigation: NavigationState,
    val errorState: MutableState<Throwable?>
) {
    val appScope = CoroutineScope(SupervisorJob() + MultifsDispatcher())

    fun launchSafe(block: suspend () -> Unit) =
        appScope.launch(MultifsDispatcher()) {
            try {
                block()
            } catch (e: Exception) {
                errorState.value = e
            }
        }

    fun launchSaveContent() {
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

    fun launchFileOpen(targetFile: File) =
        launchSafe {
            val contentBytes = targetFile.read()
            if (!editor.saving) {
                editor.content = contentBytes.decodeToString()
                editor.file = targetFile
            }
        }

    fun launchRenewFolderList(folder: FolderNode) =
        launchSafe {
            val data = folder.folder.listFolder().map { it.toFileTreeNode(folder) }
            folder.children.retainAll(data)
            folder.children.addAll(
                data.filter { !folder.children.contains(it) }
            )
        }

    fun launchInitializeStorage(namedStorageFactory: NamedStorageFactory) =
        launchSafe {
            val fs = namedStorageFactory.init()
            val name = namedStorageFactory.name
            navigation.availableStoragesState.remove(namedStorageFactory)
            navigation.configuredStoragesState.add(
                FileTree(
                    fs,
                    name,
                    FolderNode(fs.root, null, mutableStateListOf())
                )
            )
        }

    fun launchCreateFileInFolder(folder: FolderNode, filename: String) =
        launchSafe {
            val f = folder.folder.createFile(filename)
            folder.children.add(f.toFileTreeNode(folder))
        }

    fun launchCreateFolderInFolder(folder: FolderNode, foldername: String) =
        launchSafe {
            val f = folder.folder.createFolder(foldername)
            folder.children.add(f.toFileTreeNode(folder))
        }

    fun launchRemoveFolder(folder: FolderNode) =
        launchSafe {
            folder.folder.remove()
            folder.parent?.run {
                children.remove(folder)
            }
        }

    fun launchRemoveFile(file: FileNode) =
        launchSafe {
            file.file.remove()
            file.parent?.run {
                children.remove(file)
            }
        }

    fun launchCopyFileToFolder(
        fileTree: FileTree,
        file: FileNode,
        folder: FolderNode,
        filename: String,
        overwrite: Boolean
    ) =
        launchSafe {
            if (filename == "") throw Exception("target filename cannot be empty")
            fileTree.fs.copy(file.file, folder.folder, filename, overwrite)
            launchRenewFolderList(folder)
        }

    fun launchMoveFileToFolder(
        fileTree: FileTree,
        file: FileNode,
        folder: FolderNode,
        filename: String,
        overwrite: Boolean
    ) = launchSafe {
        if (filename == "") throw Exception("target filename cannot be empty")
        fileTree.fs.move(file.file, folder.folder, filename, overwrite)
        launchRenewFolderList(folder)
        file.parent?.let {
            if (it != folder) {
                launchRenewFolderList(it)
            }
        }
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
                storages.toMutableStateList(),
                mutableStateListOf()
            ),
            mutableStateOf(null)
        )
    }
}
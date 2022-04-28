package dev.salavatov.multieditor.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.salavatov.multieditor.NamedStorageFactory
import dev.salavatov.multieditor.Storage
import dev.salavatov.multieditor.expect.MultifsDispatcher
import dev.salavatov.multieditor.state.AppState
import dev.salavatov.multieditor.state.EditorState
import dev.salavatov.multifs.vfs.File
import dev.salavatov.multifs.vfs.Folder
import dev.salavatov.multifs.vfs.VFSNode
import kotlinx.coroutines.launch

@Composable
fun FolderContentView(
    children: SnapshotStateList<VFSNode>,
    editorState: EditorState,
    modifyChildren: (SnapshotStateList<VFSNode>.() -> Unit) -> Unit
): Unit = Row(
    modifier = Modifier.wrapContentHeight().fillMaxWidth()
) {
    Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(start = 15.dp)) {
        for (node in children) {
            when (node) {
                is Folder -> {
                    FolderView(node, editorState, modifyChildren)
                }
                is File -> {
                    FileView(node, editorState, modifyChildren)
                }
            }
        }
    }
}

@Composable
private fun FileView(
    file: File, editorState: EditorState,
    modifyChildren: (SnapshotStateList<VFSNode>.() -> Unit) -> Unit
) {
    val scope = rememberCoroutineScope()
    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        Icon(Icons.Default.Edit, contentDescription = null, tint = LocalContentColor.current)
        Text(file.name, modifier = Modifier.clickable {
            with(editorState) { scope.launchFileOpen(file) }
        })
        RemoveFile(file, modifyChildren)
    }
}


@Composable
fun FolderView(
    folder: Folder, editorState: EditorState,
    modifyParentChildren: (SnapshotStateList<VFSNode>.() -> Unit) -> Unit
) = Row(
    modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(vertical = 1.dp)
) {
    val coroutineScope = rememberCoroutineScope()
    val children = remember { mutableStateListOf<VFSNode>() }
    val modifyChildren: (SnapshotStateList<VFSNode>.() -> Unit) -> Unit = { block ->
        children.block()
    }

    Expandable({
        Text(folder.name)
        AddNode(folder, modifyChildren)
        RemoveFolder(folder, modifyParentChildren)
    }, {
        LaunchedEffect(key1 = Unit) {
            coroutineScope.launch(MultifsDispatcher()) { children.addAll(folder.listFolder()) }
        }
        FolderContentView(children, editorState, modifyChildren)
    })
}


@Composable
fun StorageTreeView(namedStorageFactory: NamedStorageFactory, editorState: EditorState, modifier: Modifier = Modifier) =
    Column(
        modifier = modifier.then(Modifier.wrapContentHeight().fillMaxWidth().padding(10.dp, 3.dp))
    ) {
        val scope = rememberCoroutineScope()
        val name = remember { namedStorageFactory.name }
        var storage by remember { mutableStateOf<Storage?>(null) }

        if (storage == null) {
            Row(modifier = Modifier.clickable {
                scope.launch(MultifsDispatcher()) {
                    storage = namedStorageFactory.init()
                }
            }) {
                Icon(Icons.Default.Add, null)
                Text(name)
            }
        } else {
            val root = storage!!.root
            val children = remember { mutableStateListOf<VFSNode>() }
            val modifyChildren: (SnapshotStateList<VFSNode>.() -> Unit) -> Unit = { block ->
                children.block()
            }

            LaunchedEffect(key1 = Unit) {
                scope.launch(MultifsDispatcher()) { children.addAll(root.listFolder()) }
            }

            Expandable({
                Text(name)
                AddNode(root, modifyChildren)
            }, {
                FolderContentView(children, editorState, modifyChildren)
            })
        }
    }


@Composable
fun NavigatorPane(appState: AppState, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()

    val availableBackends by remember { appState.navigation.availableStoragesState }

    Box(modifier = modifier.then(Modifier.fillMaxSize(1.0f))) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                items(availableBackends.size) { index ->
                    StorageTreeView(
                        availableBackends[index],
                        appState.editor,
                        modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(1.dp)
                    )
                }
            }
        }
    }
}

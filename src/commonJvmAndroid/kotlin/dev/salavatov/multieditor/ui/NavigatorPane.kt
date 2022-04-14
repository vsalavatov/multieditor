package dev.salavatov.multieditor.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.salavatov.multieditor.NamedStorage
import dev.salavatov.multieditor.state.AppState
import dev.salavatov.multieditor.state.EditorState
import dev.salavatov.multifs.vfs.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FolderContentView(folder: Folder, editorState: EditorState): Unit = Row(
    modifier = Modifier.wrapContentHeight().fillMaxWidth()
) {
    val coroutineScope = rememberCoroutineScope()
    val children = remember { mutableStateOf(emptyList<VFSNode>()) }

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch(Dispatchers.IO) { children.value = folder.listFolder() }
    }

    Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(start = 15.dp)) {
        for (node in children.value) {
            when (node) {
                is Folder -> {
                    FolderView(node, editorState)
                }
                is File -> {
                    FileView(node, editorState)
                }
            }
        }
    }
}

@Composable
private fun FileView(
    file: File, editorState: EditorState
) {
    val coroutineScope = rememberCoroutineScope()
    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        Icon(Icons.Default.Edit, contentDescription = null, tint = LocalContentColor.current)
        Text(file.name, modifier = Modifier.clickable {
            coroutineScope.launch(Dispatchers.IO) {
                val content = String(file.read())
                if (!editorState.saving.value) {
                    editorState.content.value = content
                    editorState.file.value = file
                }
            }
        })
        RemoveFile(file)
    }
}


@Composable
fun FolderView(folder: Folder, editorState: EditorState) = Row(
    modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(vertical = 3.dp)
) {
    Expandable({
        Text(folder.name)
        AddNode(folder)
        RemoveFolder(folder)
    }, {
        FolderContentView(folder, editorState)
    })
}


@Composable
fun FileTreeView(namedStorage: NamedStorage, editorState: EditorState, modifier: Modifier = Modifier) =
    Column(
        modifier = modifier.then(Modifier.wrapContentHeight().fillMaxWidth())
    ) {
        Expandable({
            Text(namedStorage.name)
            AddNode(namedStorage.storage.root)
        }, {
            FolderContentView(namedStorage.storage.root, editorState)
        })
    }


@Composable
fun NavigatorPane(appState: AppState, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()

    val availableBackends = remember { appState.navigation.availableStorages }
    val configuredBackends = remember { appState.navigation.configuredStorages }

    Box(modifier = modifier.then(Modifier.fillMaxSize(1.0f))) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(20.dp)) {
                availableBackends.value.forEach { storage ->
                    Button(onClick = {
                        availableBackends.value = availableBackends.value.filter { it != storage }
                        coroutineScope.launch(Dispatchers.IO) {
                            configuredBackends.value += NamedStorage(storage.name, storage.init())
                        }
                    }) {
                        Text(storage.name)
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp).fillMaxWidth())
            LazyColumn(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                items(configuredBackends.value.size) { index ->
                    FileTreeView(
                        configuredBackends.value[index],
                        appState.editor,
                        modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(1.dp)
                    )
                }
            }
        }
    }
}

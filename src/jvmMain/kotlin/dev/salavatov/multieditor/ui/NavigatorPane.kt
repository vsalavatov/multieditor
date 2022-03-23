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
import dev.salavatov.multieditor.StorageBackends
import dev.salavatov.multieditor.state.EditorState
import dev.salavatov.multifs.vfs.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FolderContentView(folder: Folder, editorState: MutableState<EditorState>): Unit = Row(
    modifier = Modifier.wrapContentHeight().fillMaxWidth()
) {
    val coroutineScope = rememberCoroutineScope()
    val children = remember { mutableStateOf(emptyList<VFSNode>()) }
    coroutineScope.launch(Dispatchers.IO) { children.value = folder.listFolder() }

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
    file: File, editorState: MutableState<EditorState>
) {
    val coroutineScope = rememberCoroutineScope()
    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        Icon(Icons.Default.Edit, contentDescription = null, tint = LocalContentColor.current)
        Text(file.name, modifier = Modifier.clickable {
            coroutineScope.launch(Dispatchers.IO) {
                val content = String(file.read())
                if (!editorState.value.saving) {
                    editorState.value = EditorState(file, content, false)
                }
            }
        })
        RemoveFile(file)
    }
}


@Composable
fun FolderView(folder: Folder, editorState: MutableState<EditorState>) = Row(
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
fun FileTreeView(name: String, fs: GenericFS, editorState: MutableState<EditorState>, modifier: Modifier = Modifier) =
    Column(
        modifier = modifier.then(Modifier.wrapContentHeight().fillMaxWidth())
    ) {
        Expandable({
            Text(name)
            AddNode(fs.root)
        }, {
            FolderContentView(fs.root, editorState)
        })
    }


@Composable
fun NavigatorPane(editorState: MutableState<EditorState>, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()

    val availableBackends = remember { mutableStateOf(StorageBackends.backends) }
    val configuredBackends = remember { mutableStateOf(emptyList<Pair<String, GenericFS>>()) }

    Box(modifier = modifier.then(Modifier.fillMaxSize(1.0f))) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(20.dp)) {
                availableBackends.value.forEach { storage ->
                    Button(onClick = {
                        availableBackends.value = availableBackends.value.filter { it != storage }
                        coroutineScope.launch(Dispatchers.IO) {
                            configuredBackends.value += storage.name to storage.init()
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
                        configuredBackends.value[index].first,
                        configuredBackends.value[index].second,
                        editorState,
                        modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(1.dp)
                    )
                }
            }
        }
    }
}

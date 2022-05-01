package dev.salavatov.multieditor.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.salavatov.multieditor.state.AppState
import dev.salavatov.multieditor.state.FileNode
import dev.salavatov.multieditor.state.FileTree
import dev.salavatov.multieditor.state.FolderNode

@Composable
fun NavigatorPane(appState: AppState, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()

    val availableStoragesState = remember { appState.navigation.availableStoragesState }
    val configuredStoragesState = remember { appState.navigation.configuredStoragesState }

    Box(modifier = modifier.then(Modifier.fillMaxSize(1.0f))) {
        LazyColumn(modifier = Modifier.fillMaxWidth().wrapContentHeight().horizontalScroll(rememberScrollState())) {
            items(availableStoragesState.size + configuredStoragesState.size) { index ->
                if (index < availableStoragesState.size) {
                    val storageFactory = availableStoragesState[index]
                    val name = storageFactory.name
                    Row(modifier = Modifier.clickable {
                        with(appState) {
                            scope.launchInitializeStorage(storageFactory)
                        }
                    }) {
                        Icon(Icons.Default.Add, null)
                        Text(name)
                    }
                } else {
                    val fileTree = configuredStoragesState[index - availableStoragesState.size]
                    Expandable({
                        Text(fileTree.name)
                        AddNode(appState, fileTree.root)
                    }, {
                        FolderContentView(appState, fileTree, fileTree.root)
                    }, onExpand = {
                        with(appState) {
                            scope.launchRenewFolderList(fileTree.root)
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun FolderContentView(
    appState: AppState,
    fileTree: FileTree,
    folder: FolderNode
): Unit =
    Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(start = 15.dp)) {
        folder.children.forEach { node ->
            when (node) {
                is FileNode -> FileView(appState, fileTree, node)
                is FolderNode -> FolderView(appState, fileTree, node)
            }
        }
    }


@Composable
private fun FileView(
    appState: AppState,
    fileTree: FileTree,
    file: FileNode
) {
    val scope = rememberCoroutineScope()
    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        Icon(Icons.Default.Edit, contentDescription = null, tint = LocalContentColor.current)
        Text(file.file.name, modifier = Modifier.clickable {
            with(appState) { scope.launchFileOpen(file.file) }
        })
        RemoveFile(appState, file)
        MoveCopyFile(appState, fileTree, file)
    }
}


@Composable
fun FolderView(
    appState: AppState,
    fileTree: FileTree,
    folder: FolderNode
) = Row(
    modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(vertical = 1.dp)
) {
    val scope = rememberCoroutineScope()
    Expandable({
        Text(folder.folder.name)
        AddNode(appState, folder)
        RemoveFolder(appState, folder)
    }, {
        FolderContentView(appState, fileTree, folder)
    }, onExpand = {
        with(appState) {
            scope.launchRenewFolderList(folder)
        }
    })
}

package dev.salavatov.multieditor.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.salavatov.multieditor.expect.AlertDialog
import dev.salavatov.multieditor.state.AppState
import dev.salavatov.multieditor.state.FileNode
import dev.salavatov.multieditor.state.FileTree
import dev.salavatov.multieditor.state.FolderNode

@Composable
fun AddNode(appState: AppState, folder: FolderNode) {
    var showDialog: Boolean by remember { mutableStateOf(false) }
    var filename by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = "enter name",
            content = {
                Column(modifier = Modifier.wrapContentSize()) {
                    TextField(
                        filename,
                        { filename = it },
                        modifier = Modifier.width(300.dp).wrapContentHeight().padding(2.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.padding(3.dp)) {
                        Button(onClick = {
                            val fname = filename
                            if (fname != "") {
                                appState.launchCreateFileInFolder(folder, fname)
                                showDialog = false
                            }
                        }) { Text("add file") }
                        Spacer(modifier = Modifier.width(20.dp))
                        Button(onClick = {
                            val fname = filename
                            if (fname != "") {
                                appState.launchCreateFolderInFolder(folder, fname)
                                showDialog = false
                            }
                        }) { Text("add folder") }
                    }
                }
            })
    }
    Icon(Icons.Default.Add, "add file or folder", modifier = Modifier.clickable { showDialog = true })
}

@Composable
fun RemoveFolder(appState: AppState, folder: FolderNode) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = "folder remove",
            content = {
                Text("are you sure you want to remove ${folder.folder.name}?", modifier = Modifier.fillMaxWidth())
                Row(modifier = Modifier.padding(3.dp)) {
                    Button(onClick = {
                        appState.launchRemoveFolder(folder)
                        showDialog = false
                    }) { Text("sure!") }
                }
            }
        )
    }
    Icon(Icons.Default.Delete, "delete", modifier = Modifier.clickable { showDialog = true })
}

@Composable
fun RemoveFile(appState: AppState, file: FileNode) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = "file remove",
            content = {
                Column(modifier = Modifier.wrapContentSize()) {
                    Text("are you sure you want to remove ${file.file.name}?", modifier = Modifier.fillMaxWidth())
                    Row(modifier = Modifier.padding(3.dp)) {
                        Button(onClick = {
                            appState.launchRemoveFile(file)
                            showDialog = false
                        }) { Text("sure!") }
                    }
                }
            }
        )
    }
    Icon(Icons.Default.Delete, "delete", modifier = Modifier.clickable { showDialog = true })
}

@Composable
fun MoveCopyFile(appState: AppState, fileTree: FileTree, file: FileNode) {
    var showDialog by remember { mutableStateOf(false) }
    var targetFilename by remember { mutableStateOf(file.file.name) }
    var overwriteFlag by remember { mutableStateOf(false) }
    if (showDialog) {
        var currentFolder by mutableStateOf(fileTree.root)
        val scrollState = rememberScrollState(0)
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = "move or copy file",
            content = {
                LaunchedEffect(currentFolder) {
                    appState.launchRenewFolderList(currentFolder)
                }
                val element = Modifier.padding(2.dp).wrapContentSize()
                Column(
                    modifier = Modifier.fillMaxHeight(0.7f).verticalScroll(scrollState),
                    verticalArrangement = Arrangement.Top
                ) {
                    Icon(Icons.Default.Refresh, "refresh list", modifier = element.clickable {
                        appState.launchRenewFolderList(currentFolder)
                    })
                    currentFolder.parent?.let { parent ->
                        Row(modifier = element.clickable {
                            currentFolder = parent
                        }) {
                            Icon(Icons.Default.ArrowBack, "to parent")
                            Text("..")
                        }
                    }
                    currentFolder.children.forEach { node ->
                        when (node) { // val node = currentFolder.children[index]
                            is FileNode -> Row(modifier = element.clickable {
                                targetFilename = node.file.name
                            }) {
                                Icon(Icons.Default.Edit, "file")
                                Text(node.file.name)
                            }
                            is FolderNode -> Row(modifier = element.clickable {
                                currentFolder = node
                            }) {
                                Icon(Icons.Default.List, "file")
                                Text(node.folder.name)
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    TextField(
                        targetFilename,
                        { targetFilename = it },
                        modifier = Modifier.width(250.dp).wrapContentHeight(),
                        singleLine = true
                    )
                    Row(modifier = Modifier.padding(2.dp).wrapContentSize()) {
                        Checkbox(overwriteFlag, { overwriteFlag = it })
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("overwrite", textAlign = TextAlign.Center)
                    }
                    Row(modifier = Modifier.padding(2.dp).wrapContentSize()) {
                        Button(onClick = {
                            appState.launchCopyFileToFolder(
                                fileTree,
                                file,
                                currentFolder,
                                targetFilename,
                                overwriteFlag
                            )
                            showDialog = false
                        }) { Text("copy") }
                        Spacer(modifier = Modifier.width(20.dp))
                        Button(onClick = {
                            appState.launchMoveFileToFolder(
                                fileTree,
                                file,
                                currentFolder,
                                targetFilename,
                                overwriteFlag
                            )
                            showDialog = false
                        }) { Text("move") }
                    }
                }
            }
        )
    }
    Icon(Icons.Default.Share, "move or copy", modifier = Modifier.clickable { showDialog = true })
}
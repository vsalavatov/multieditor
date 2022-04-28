package dev.salavatov.multieditor.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.salavatov.multieditor.expect.AlertDialog
import dev.salavatov.multieditor.expect.MultifsDispatcher
import dev.salavatov.multifs.vfs.File
import dev.salavatov.multifs.vfs.Folder
import dev.salavatov.multifs.vfs.VFSNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddNode(folder: Folder, modifyChildren: (SnapshotStateList<VFSNode>.() -> Unit) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog: Boolean by remember { mutableStateOf(false) }
    var filename by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            buttons = {
                Button(onClick = {
                    val fname = filename
                    if (fname != "") {
                        coroutineScope.launch(Dispatchers.IO) {
                            val f = folder.createFile(fname)
                            modifyChildren {
                                add(f)
                            }
                        }
                        showDialog = false
                    }
                }) { Text("add file") }
                Spacer(modifier = Modifier.width(20.dp))
                Button(onClick = {
                    val fname = filename
                    if (fname != "") {
                        coroutineScope.launch(MultifsDispatcher()) {
                            val f = folder.createFolder(fname)
                            modifyChildren {
                                add(f)
                            }
                        }
                        showDialog = false
                    }
                }) { Text("add folder") }
            },
            modifier = Modifier.wrapContentSize(),
            title = { Text("enter name") },
            content = {
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    filename,
                    { filename = it },
                    modifier = Modifier.width(300.dp).wrapContentHeight(),
                    singleLine = true
                )
            })
    }
    Icon(Icons.Default.Add, null, modifier = Modifier.clickable { showDialog = true })
}

@Composable
fun RemoveFolder(folder: Folder, modifyChildren: (SnapshotStateList<VFSNode>.() -> Unit) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            buttons = {
                Button(onClick = {
                    coroutineScope.launch(MultifsDispatcher()) {
                        folder.remove()
                        modifyChildren {
                            removeAll { it == folder }
                        }
                    }
                    showDialog = false
                }) { Text("sure!") }
            },
            modifier = Modifier.wrapContentSize(),
            title = { Text("folder remove") },
            content = {
                Text("are you sure you want to remove ${folder.name}?", modifier = Modifier.width(300.dp))
            }
        )
    }
    Icon(Icons.Default.Delete, null, modifier = Modifier.clickable { showDialog = true })
}

@Composable
fun RemoveFile(file: File, modifyChildren: (SnapshotStateList<VFSNode>.() -> Unit) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            buttons = {
                Button(onClick = {
                    coroutineScope.launch(MultifsDispatcher()) {
                        file.remove()
                        modifyChildren {
                            removeAll { it == file }
                        }
                    }
                    showDialog = false
                }) { Text("sure!") }
            },
            modifier = Modifier.wrapContentSize(),
            title = { Text("file remove") },
            content = {
                Text("are you sure you want to remove ${file.name}?", modifier = Modifier.width(300.dp))
            }
        )
    }
    Icon(Icons.Default.Delete, null, modifier = Modifier.clickable { showDialog = true })
}
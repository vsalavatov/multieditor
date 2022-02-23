package dev.salavatov.multieditor.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.salavatov.multifs.vfs.File
import dev.salavatov.multifs.vfs.Folder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddNode(folder: Folder) {
    val coroutineScope = rememberCoroutineScope()
    val showDialog = remember { mutableStateOf(false) }
    val filename = remember { mutableStateOf("") }
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
            },
            buttons = {
                Button(onClick = {
                    val fname = filename.value
                    if (fname != "") {
                        coroutineScope.launch(Dispatchers.IO) {
                            folder.createFile(fname)
                        }
                        showDialog.value = false
                    }
                }) { Text("add file") }
                Spacer(modifier = Modifier.width(20.dp))
                Button(onClick = {
                    val fname = filename.value
                    if (fname != "") {
                        coroutineScope.launch(Dispatchers.IO) {
                            folder.createFolder(fname)
                        }
                        showDialog.value = false
                    }
                }) { Text("add folder") }
            },
            modifier = Modifier.wrapContentSize(),
            title = { Text("enter name") },
            text = {
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    filename.value,
                    { filename.value = it },
                    modifier = Modifier.width(300.dp).wrapContentHeight(),
                    singleLine = true
                )
            })
    }
    Icon(Icons.Default.Add, null, modifier = Modifier.clickable { showDialog.value = true })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RemoveFolder(folder: Folder) {
    val coroutineScope = rememberCoroutineScope()
    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
            },
            buttons = {
                Button(onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        folder.remove()
                    }
                    showDialog.value = false
                }) { Text("sure!") }
            },
            modifier = Modifier.wrapContentSize(),
            title = { Text("folder remove") },
            text = {
                Text("are you sure you want to remove ${folder.name}?", modifier = Modifier.width(300.dp))
            }
        )
    }
    Icon(Icons.Default.Delete, null, modifier = Modifier.clickable { showDialog.value = true })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RemoveFile(file: File) {
    val coroutineScope = rememberCoroutineScope()
    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
            },
            buttons = {
                Button(onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        file.remove()
                    }
                    showDialog.value = false
                }) { Text("sure!") }
            },
            modifier = Modifier.wrapContentSize(),
            title = { Text("file remove") },
            text = {
                Text("are you sure you want to remove ${file.name}?", modifier = Modifier.width(300.dp))
            }
        )
    }
    Icon(Icons.Default.Delete, null, modifier = Modifier.clickable { showDialog.value = true })
}
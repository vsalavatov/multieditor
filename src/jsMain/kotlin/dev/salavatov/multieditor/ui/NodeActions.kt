package dev.salavatov.multieditor.ui

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import dev.salavatov.multieditor.expect.MultifsDispatcher
import dev.salavatov.multifs.vfs.File
import dev.salavatov.multifs.vfs.Folder
import dev.salavatov.multifs.vfs.VFSNode
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea

object NodeActions : StyleSheet() {
    val addNode by style {
        self + after style {
            property("content", "\"➕\"")
            display(DisplayStyle.InlineBlock)
            marginLeft(5.px)
        }
    }

    val deleteNode by style {
        self + after style {
            property("content", "\"♲\"")
            display(DisplayStyle.InlineBlock)
            marginLeft(5.px)
        }
    }
}

@Composable
fun AddNode(folder: Folder, modifyChildren: (SnapshotStateList<VFSNode>.() -> Unit) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog: Boolean by remember { mutableStateOf(false) }
    var filename by remember { mutableStateOf("") }

    if (showDialog) {
        ModalDialog(
            onDismissRequest = {
                showDialog = false
            },
            content = {
                Div {
                    TextArea(filename) {
                        onInput { filename = it.value }
                        style {
                            width(200.px)
                        }
                    }
                }
                Div {
                    SpanButton({
                        val fname = filename
                        if (fname != "") {
                            coroutineScope.launch(MultifsDispatcher()) {
                                val file = folder.createFile(fname)
                                modifyChildren {
                                    add(file)
                                }
                            }
                            showDialog = false
                        }
                    }) { Text("add file") }
                    SpanButton({
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
                }
            },
            title = { Text("enter name") },
        )
    }
    Span({
        onClick { showDialog = true }
        classes(NodeActions.addNode)
    }) {}
}

@Composable
fun RemoveFolder(folder: Folder, modifyChildren: (SnapshotStateList<VFSNode>.() -> Unit) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        ModalDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = { Text("folder remove") },
            content = {
                Div { Text("are you sure you want to remove ${folder.name}?") }
                SpanButton({
                    coroutineScope.launch(MultifsDispatcher()) {
                        folder.remove()
                        modifyChildren {
                            removeAll { it == folder }
                        }
                    }
                    showDialog = false
                }) { Text("sure!") }
            }
        )
    }
    Span({
        onClick { showDialog = true }
        classes(NodeActions.deleteNode)
    }) {}
}

@Composable
fun RemoveFile(file: File, modifyChildren: (SnapshotStateList<VFSNode>.() -> Unit) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        ModalDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = { Text("file remove") },
            content = {
                Div { Text("are you sure you want to remove ${file.name}?") }
                SpanButton({
                    coroutineScope.launch(MultifsDispatcher()) {
                        file.remove()
                        modifyChildren {
                            removeAll { it == file }
                        }
                    }
                    showDialog = false
                }) { Text("sure!") }
            }
        )
    }
    Span({
        onClick { showDialog = true }
        classes(NodeActions.deleteNode)
    }) {}
}
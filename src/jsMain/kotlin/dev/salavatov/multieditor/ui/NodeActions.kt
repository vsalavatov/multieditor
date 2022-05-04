package dev.salavatov.multieditor.ui

import androidx.compose.runtime.*
import dev.salavatov.multieditor.state.AppState
import dev.salavatov.multieditor.state.FileNode
import dev.salavatov.multieditor.state.FileTree
import dev.salavatov.multieditor.state.FolderNode
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

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
            property("content", "\"⨯\"")
            display(DisplayStyle.InlineBlock)
            marginLeft(5.px)
        }
    }

    val moveCopyFile by style {
        self + after style {
            property("content", "\"\uF1E0\"")
            display(DisplayStyle.InlineBlock)
            marginLeft(5.px)
        }
    }

    val moveCopyElem by style {
        padding(2.px)
        self + hover style {
            backgroundColor(Color.lightgray)
        }
    }

    val refresh by style {
        self + before style {
            property("content", "\"↺\"")
            display(DisplayStyle.InlineBlock)
            marginRight(5.px)
        }
    }

    val back by style {
        self + before style {
            property("content", "\"←\"")
            display(DisplayStyle.InlineBlock)
            marginRight(5.px)
        }
    }
}

@Composable
fun AddNode(appState: AppState, folder: FolderNode) {
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
                            appState.launchCreateFileInFolder(folder, fname)
                            showDialog = false
                        }
                    }) { Text("add file") }
                    SpanButton({
                        val fname = filename
                        if (fname != "") {
                            appState.launchCreateFolderInFolder(folder, fname)
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
fun RemoveFolder(appState: AppState, folder: FolderNode) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        ModalDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = { Text("folder remove") },
            content = {
                Div { Text("are you sure you want to remove ${folder.folder.name}?") }
                SpanButton({
                    appState.launchRemoveFolder(folder)
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
fun RemoveFile(appState: AppState, file: FileNode) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        ModalDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = { Text("file remove") },
            content = {
                Div { Text("are you sure you want to remove ${file.file.name}?") }
                SpanButton({
                    appState.launchRemoveFile(file)
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
fun MoveCopyFile(appState: AppState, fileTree: FileTree, file: FileNode) {
    var showDialog by remember { mutableStateOf(false) }
    var targetFilename by remember { mutableStateOf(file.file.name) }
    var overwriteFlag by remember { mutableStateOf(false) }
    if (showDialog) {
        var currentFolder by mutableStateOf(fileTree.root)
        ModalDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = { Text("move or copy file") },
            content = {
                LaunchedEffect(currentFolder) {
                    appState.launchRenewFolderList(currentFolder)
                }
                Div({
                    style {
                        padding(3.px)
                        maxHeight(400.px)
                        overflowY("scroll")
                    }
                }) {
                    Div({
                        classes(NodeActions.refresh, NodeActions.moveCopyElem)
                    }) {}
                    currentFolder.parent?.let { parent ->
                        Div({
                            onClick {
                                currentFolder = parent
                            }
                            classes(NodeActions.back, NodeActions.moveCopyElem)
                        }) {
                            Text("..")
                        }
                    }
                    currentFolder.children.forEach { node ->
                        when (node) {
                            is FileNode -> Div({
                                onClick {
                                    targetFilename = node.file.name
                                }
                                classes(NavigatorPaneStyle.fileIcon, NodeActions.moveCopyElem)
                            }) {
                                Text(node.file.name)
                            }
                            is FolderNode -> Div({
                                onClick {
                                    currentFolder = node
                                }
                                classes(NavigatorPaneStyle.folderIcon, NodeActions.moveCopyElem)
                            }) {
                                Text(node.folder.name)
                            }
                        }
                    }
                }

                Div({
                    style {
                        padding(3.px)
                    }
                }) {
                    Div {
                        TextArea(targetFilename) {
                            onInput { targetFilename = it.value }
                            style {
                                width(200.px)
                            }
                        }
                    }
                    Span {
                        CheckboxInput(overwriteFlag) {
                            onClick { overwriteFlag = !overwriteFlag }
                            style {
                                paddingRight(
                                    15.px
                                )
                            }
                        }
                        Text("overwrite")
                    }
                    Span {
                        SpanButton({
                            appState.launchCopyFileToFolder(
                                fileTree,
                                file,
                                currentFolder,
                                targetFilename,
                                overwriteFlag
                            )
                            showDialog = false
                        }, attrs = {
                            style {
                                paddingRight(20.px)
                            }
                        }) { Text("copy") }
                        SpanButton({
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
    Span({
        onClick { showDialog = true }
        classes(NodeActions.moveCopyFile)
    })
}
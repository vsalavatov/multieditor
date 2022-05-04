package dev.salavatov.multieditor.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.salavatov.multieditor.state.AppState
import dev.salavatov.multieditor.state.FileNode
import dev.salavatov.multieditor.state.FileTree
import dev.salavatov.multieditor.state.FolderNode
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

object NavigatorPaneStyle : StyleSheet() {
    val addStorage by style {
        self + before style {
            property("content", "\"➕\"")
            display(DisplayStyle.InlineBlock)
            marginRight(5.px)
        }
    }

    val fileIcon by style {
        self + before style {
            property("content", "\"✏\"")
            display(DisplayStyle.InlineBlock)
            marginRight(5.px)
        }
    }

    val folderIcon by style {
        self + before style {
            property("content", "\"☰\"")
            display(DisplayStyle.InlineBlock)
            marginRight(5.px)
        }
    }
}

@Composable
fun NavigatorPane(appState: AppState) {
    val availableStoragesState = remember { appState.navigation.availableStoragesState }
    val configuredStoragesState = remember { appState.navigation.configuredStoragesState }
    Div({
        style {
            marginLeft(2.px)
            marginRight(4.px)
        }
    }) {
        availableStoragesState.forEach { namedStorageFactory ->
            val name = namedStorageFactory.name
            Div({
                style {
                    position(Position.Relative)
                    width(100.percent)
                    height(auto)
                }
                onClick {
                    appState.launchInitializeStorage(namedStorageFactory)
                }
                classes(NavigatorPaneStyle.addStorage)
            }) {
                Text(name)
            }
        }

        configuredStoragesState.forEach { fileTree ->
            Expandable({
                Text(fileTree.name)
            }, {
                AddNode(appState, fileTree.root)
                FolderContentView(appState, fileTree, fileTree.root)
            }, onExpand = {
                appState.launchRenewFolderList(fileTree.root)
            })
        }
    }
}

@Composable
fun FolderContentView(
    appState: AppState,
    fileTree: FileTree,
    folder: FolderNode
): Unit =
    Div({
        style {
            position(Position.Relative)
            width(100.percent)
            height(auto)
        }
    }) {
        Div({
            style {
                width(100.percent)
                height(auto)
                marginTop(3.px)
                marginBottom(3.px)
                marginLeft(10.px)
            }
        }) {
            folder.children.forEach { node ->
                when (node) {
                    is FolderNode -> {
                        FolderView(appState, fileTree, node)
                    }
                    is FileNode -> {
                        FileView(appState, fileTree, node)
                    }
                }
            }
        }
    }


@Composable
private fun FileView(
    appState: AppState,
    fileTree: FileTree,
    file: FileNode
) {
    Div({
        style {
            position(Position.Relative)
            width(100.percent)
            height(auto)
            padding(3.px)
        }
    }) {
        Span({
            onClick {
                appState.launchFileOpen(file.file)
            }
            classes(NavigatorPaneStyle.fileIcon)
        }) { Text(file.file.name) }
        RemoveFile(appState, file)
        MoveCopyFile(appState, fileTree, file)
    }
}


@Composable
fun FolderView(
    appState: AppState,
    fileTree: FileTree,
    folder: FolderNode
): Unit = Div({
    style {
        position(Position.Relative)
        width(100.percent)
        height(auto)
        padding(3.px)
    }
}) {
    Expandable({
        Text(folder.folder.name)
    }, {
        AddNode(appState, folder)
        RemoveFolder(appState, folder)
        FolderContentView(appState, fileTree, folder)
    }, onExpand = {
        appState.launchRenewFolderList(folder)
    })
}

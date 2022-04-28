package dev.salavatov.multieditor.ui

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import dev.salavatov.multieditor.NamedStorageFactory
import dev.salavatov.multieditor.Storage
import dev.salavatov.multieditor.expect.MultifsDispatcher
import dev.salavatov.multieditor.state.AppState
import dev.salavatov.multieditor.state.EditorState
import dev.salavatov.multifs.vfs.File
import dev.salavatov.multifs.vfs.Folder
import dev.salavatov.multifs.vfs.VFSNode
import kotlinx.coroutines.launch
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
}

@Composable
fun NavigatorPane(appState: AppState) {
    val availableBackends by remember { appState.navigation.availableStoragesState }

    availableBackends.forEach { namedStorageFactory ->
        Div({
            style {
                padding(5.px)
            }
        }) {
            StorageTreeView(
                namedStorageFactory,
                appState.editor,
            )
        }
    }
}

@Composable
fun StorageTreeView(namedStorageFactory: NamedStorageFactory, editorState: EditorState): Unit =
    Div({
        style {
            position(Position.Relative)
            width(100.percent)
            height(auto)
        }
    }) {
        val scope = rememberCoroutineScope()
        val name = remember { namedStorageFactory.name }
        var storage by remember { mutableStateOf<Storage?>(null) }

        if (storage == null) {
            Span({
                onClick {
                    scope.launch(MultifsDispatcher()) {
                        storage = namedStorageFactory.init()
                    }
                }
                classes(NavigatorPaneStyle.addStorage)
            }) {
                Text(name)
            }
        } else {
            val root = remember { storage!!.root }
            val childrenState = remember { mutableStateListOf<VFSNode>() }

            val modifyChildren: (SnapshotStateList<VFSNode>.() -> Unit) -> Unit = { block ->
                childrenState.block()
            }

            LaunchedEffect(key1 = Unit) {
                scope.launch(MultifsDispatcher()) {
                    childrenState.addAll(root.listFolder())
                }
            }

            Expandable({
                Text(name)
            }) {
                AddNode(root, modifyChildren)
                FolderContentView(childrenState, editorState, modifyChildren)
            }
        }
    }


@Composable
fun FolderContentView(
    children: SnapshotStateList<VFSNode>,
    editorState: EditorState,
    modifyChildren: (SnapshotStateList<VFSNode>.() -> Unit) -> Unit
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
            children.forEach { node ->
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
    file: File, editorState: EditorState, modifyChildren: (SnapshotStateList<VFSNode>.() -> Unit) -> Unit
): Unit {
    val scope = rememberCoroutineScope()
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
                with(editorState) { scope.launchFileOpen(file) }
            }
            classes(NavigatorPaneStyle.fileIcon)
        }) { Text(file.name) }
        RemoveFile(file, modifyChildren)
    }
}


@Composable
fun FolderView(
    folder: Folder,
    editorState: EditorState,
    modifyParentChildren: (SnapshotStateList<VFSNode>.() -> Unit) -> Unit
): Unit = Div({
    style {
        position(Position.Relative)
        width(100.percent)
        height(auto)
        padding(3.px)
    }
}) {
    val coroutineScope = rememberCoroutineScope()
    val childrenState = remember { mutableStateListOf<VFSNode>() }

    val modifyChildren : (SnapshotStateList<VFSNode>.() -> Unit) -> Unit = { block ->
        childrenState.block()
    }

    Expandable({
        Text(folder.name)
    }, {
        LaunchedEffect(key1 = Unit) {
            coroutineScope.launch(MultifsDispatcher()) { childrenState.addAll(folder.listFolder()) }
        }

        RemoveFolder(folder, modifyParentChildren)
        AddNode(folder, modifyChildren)
        FolderContentView(childrenState, editorState, modifyChildren)
    })
}

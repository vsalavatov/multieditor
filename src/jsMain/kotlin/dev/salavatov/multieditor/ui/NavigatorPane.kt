package dev.salavatov.multieditor.ui

import androidx.compose.runtime.*
import dev.salavatov.multieditor.StorageBackends
import dev.salavatov.multieditor.state.EditorState
import dev.salavatov.multifs.vfs.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun NavigatorPane(editorState: MutableState<EditorState>) {
    val coroutineScope = rememberCoroutineScope()

    val availableBackends = remember { mutableStateOf(StorageBackends.backends) }
    val configuredBackends = remember { mutableStateOf(emptyList<Pair<String, GenericFS>>()) }

    Div({
        style {
            width(100.percent)
            height(auto)
        }
    }) {
        availableBackends.value.forEach { storage ->
            Button(attrs = {
                style {
                    padding(20.px)
                }
                onClick {
                    availableBackends.value = availableBackends.value.filter { it != storage }
                    coroutineScope.launch(Dispatchers.Unconfined) { // TODO: ???
                        configuredBackends.value += storage.name to storage.init()
                    }
                }
            }) {
                Text(storage.name)
            }
        }
        Div({
            style {
                height(30.px)
            }
        }) {}
        configuredBackends.value.forEachIndexed { index, _ ->
            FileTreeView(
                configuredBackends.value[index].first,
                configuredBackends.value[index].second,
                editorState,
            )
        }
    }
}

@Composable
fun FileTreeView(name: String, fs: GenericFS, editorState: MutableState<EditorState>): Unit =
    Div({
        style {
            width(100.percent)
            height(auto)
            padding(20.px)
        }
    }) {
        Expandable({
            Span({ style { textDecoration("bold") } }) { Text(name) }
//            AddNode(fs.root)
        }, {
            FolderContentView(fs.root, editorState)
        })
    }


@Composable
fun FolderContentView(folder: Folder, editorState: MutableState<EditorState>): Unit = Div({
    style {
        width(100.percent)
        height(auto)
    }
}) {
    val coroutineScope = rememberCoroutineScope()
    val children = remember { mutableStateOf(emptyList<VFSNode>()) }
    coroutineScope.launch(Dispatchers.Unconfined) { children.value = folder.listFolder() }

    Div({
        style {
            width(100.percent)
            height(auto)
            padding(15.px)
        }
    }) {
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
): Unit {
    val coroutineScope = rememberCoroutineScope()
    Div({
        style {
            width(100.percent)
            height(auto)
            padding(2.px)
        }
    }) {
//        Icon(Icons.Default.Edit, contentDescription = null, tint = LocalContentColor.current)
        Span({
            onClick {
                coroutineScope.launch(Dispatchers.Unconfined) {
                    val content = file.read().decodeToString()
                    if (!editorState.value.saving) {
                        editorState.value = EditorState(file, content, false)
                    }
                }
            }
        }) { Text(file.name) }
//        RemoveFile(file)
    }
}


@Composable
fun FolderView(folder: Folder, editorState: MutableState<EditorState>): Unit = Div({
    style {
        width(100.percent)
        height(auto)
        padding(5.px)
    }
}) {
    Expandable({
        Text(folder.name)
//        AddNode(folder)
//        RemoveFolder(folder)
    }, {
        FolderContentView(folder, editorState)
    })
}

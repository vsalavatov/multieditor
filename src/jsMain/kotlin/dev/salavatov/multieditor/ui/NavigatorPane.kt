package dev.salavatov.multieditor.ui

import androidx.compose.runtime.*
import dev.salavatov.multieditor.StorageBackends
import dev.salavatov.multieditor.state.EditorState
import dev.salavatov.multifs.vfs.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.maxWidth
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Text

@Composable
fun NavigatorPane(editorState: MutableState<EditorState>) {
    val coroutineScope = rememberCoroutineScope()

    val availableBackends = remember { mutableStateOf(StorageBackends.backends) }
    val configuredBackends = remember { mutableStateOf(emptyList<Pair<String, GenericFS>>()) }

    Div({
        style { maxWidth("30%") }
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
        }) {  }
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
fun FileTreeView(name: String, fs: GenericFS, editorState: MutableState<EditorState>) =
    Div({

    }) {
        Text(name)
//        Expandable({
//            Text(name)
//            AddNode(fs.root)
//        }, {
//            FolderContentView(fs.root, editorState)
//        })
    }

//
//@Composable
//fun FolderContentView(folder: Folder, editorState: MutableState<EditorState>): Unit = Row(
//    modifier = Modifier.wrapContentHeight().fillMaxWidth()
//) {
//    val coroutineScope = rememberCoroutineScope()
//    val children = remember { mutableStateOf(emptyList<VFSNode>()) }
//    coroutineScope.launch(Dispatchers.IO) { children.value = folder.listFolder() }
//
//    Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(start = 15.dp)) {
//        for (node in children.value) {
//            when (node) {
//                is Folder -> {
//                    FolderView(node, editorState)
//                }
//                is File -> {
//                    FileView(node, editorState)
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//private fun FileView(
//    file: File, editorState: MutableState<EditorState>
//) {
//    val coroutineScope = rememberCoroutineScope()
//    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
//        Icon(Icons.Default.Edit, contentDescription = null, tint = LocalContentColor.current)
//        Text(file.name, modifier = Modifier.clickable {
//            coroutineScope.launch(Dispatchers.IO) {
//                val content = kotlin.text.String(file.read())
//                if (!editorState.value.saving) {
//                    editorState.value = EditorState(file, content, false)
//                }
//            }
//        })
//        RemoveFile(file)
//    }
//}
//
//
//@Composable
//fun FolderView(folder: Folder, editorState: MutableState<EditorState>) = Row(
//    modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(vertical = 3.dp)
//) {
//    Expandable({
//        Text(folder.name)
//        AddNode(folder)
//        RemoveFolder(folder)
//    }, {
//        FolderContentView(folder, editorState)
//    })
//}

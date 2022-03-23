package dev.salavatov.multieditor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import dev.salavatov.multieditor.state.EditorState
import dev.salavatov.multifs.vfs.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditorPane(editorState: MutableState<EditorState>, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.then(Modifier.fillMaxSize(1.0f)).onPreviewKeyEvent {
        if (it.isCtrlPressed && it.key == Key.S) {
            val state = editorState.value
            if (state.file != null && !state.saving) {
                editorState.value = EditorState(state.file, state.content, true)
                coroutineScope.launch(Dispatchers.IO) {
                    state.file.write(state.content.toByteArray())
                }.invokeOnCompletion {
                    editorState.value = EditorState(state.file, state.content, false)
                }
            }
            true
        } else {
            false
        }
    }) {
        Row(modifier = Modifier.fillMaxWidth(1f).wrapContentHeight()) {
            Text(editorState.value.file?.name ?: "", modifier = Modifier.fillMaxWidth(0.5f))
            Text(if (editorState.value.saving) { "saving..." } else { "" })
        }
        Divider(modifier = Modifier.fillMaxWidth(), color = Color.LightGray)
        CompositionLocalProvider(LocalTextSelectionColors provides TextSelectionColors(Color.White, Color.LightGray)) {
            TextField(
                editorState.value.content,
                { editorState.value = EditorState(editorState.value.file, it) },
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                colors = TextFieldDefaults.outlinedTextFieldColors(cursorColor = Color.DarkGray),
                placeholder = { Text("content...", color = Color.LightGray) }
            )
        }
    }
}
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
import dev.salavatov.multieditor.state.AppState
import dev.salavatov.multieditor.state.EditorState
import dev.salavatov.multifs.vfs.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditorPane(appState: AppState, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()

    val editorState = remember { appState.editor }

    Column(modifier = modifier.then(Modifier.fillMaxSize(1.0f)).onPreviewKeyEvent {
        if (it.isCtrlPressed && it.key == Key.S) {
            val file = editorState.file.value
            if (file != null && !editorState.saving.value) {
                editorState.saving.value = true
                coroutineScope.launch(Dispatchers.IO) {
                    file.write(editorState.content.value.toByteArray())
                }.invokeOnCompletion {
                    editorState.saving.value = false
                }
            }
            true
        } else {
            false
        }
    }) {
        Row(modifier = Modifier.fillMaxWidth(1f).wrapContentHeight()) {
            Text(editorState.file.value?.name ?: "", modifier = Modifier.fillMaxWidth(0.5f))
            Text(if (editorState.saving.value) { "saving..." } else { "" })
        }
        Divider(modifier = Modifier.fillMaxWidth(), color = Color.LightGray)
        CompositionLocalProvider(LocalTextSelectionColors provides TextSelectionColors(Color.White, Color.LightGray)) {
            TextField(
                editorState.content.value,
                { editorState.content.value = it },
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                colors = TextFieldDefaults.outlinedTextFieldColors(cursorColor = Color.DarkGray),
                placeholder = { Text("content...", color = Color.LightGray) }
            )
        }
    }
}
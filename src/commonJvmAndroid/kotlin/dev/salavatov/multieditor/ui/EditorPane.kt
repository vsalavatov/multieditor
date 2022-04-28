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
import androidx.compose.ui.unit.dp
import dev.salavatov.multieditor.state.AppState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditorPane(appState: AppState, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val editorState = remember { appState.editor }

    Column(modifier = modifier.then(Modifier.fillMaxSize(1.0f)).onPreviewKeyEvent {
        if (it.isCtrlPressed && it.key == Key.S) {
            with(appState.editor) { coroutineScope.launchSaveContent() }
            true
        } else {
            false
        }
    }) {
        Row(modifier = Modifier.fillMaxWidth(1f).wrapContentHeight()) {
            Text(editorState.file?.name ?: "", modifier = Modifier.fillMaxWidth(0.5f).padding(10.dp))
            Button(
                onClick = { with(appState.editor) { coroutineScope.launchSaveContent() } }
            ) { Text("save") }
            Text(
                if (editorState.saving) {
                    "saving..."
                } else {
                    ""
                },
                modifier = Modifier.padding(10.dp)
            )
        }
        Divider(modifier = Modifier.fillMaxWidth(), color = Color.LightGray)
        CompositionLocalProvider(LocalTextSelectionColors provides TextSelectionColors(Color.White, Color.LightGray)) {
            TextField(
                editorState.content,
                { editorState.content = it },
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                colors = TextFieldDefaults.outlinedTextFieldColors(cursorColor = Color.DarkGray),
                placeholder = { Text("type here...", color = Color.LightGray) }
            )
        }
    }
}
package dev.salavatov.multieditor.ui

import androidx.compose.runtime.*
import dev.salavatov.multieditor.state.AppState
import dev.salavatov.multieditor.state.EditorState
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.TextAreaWrap
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.wrap
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*


@Composable
fun EditorPane(appState: AppState) {
    val coroutineScope = rememberCoroutineScope()
    val editorState = remember { appState.editor }

    Div({
        style {
            height(100.percent)
            width(100.percent)
            display(DisplayStyle.InlineBlock)
        }
    }) {
        Span {
            Button({
                onClick {
                    console.log("click", editorState.file.value, editorState.saving.value)
                    val file = editorState.file.value
                    if (file != null && !editorState.saving.value) {
                        editorState.saving.value = true
                        coroutineScope.launch(Dispatchers.Unconfined) {
                            file.write(editorState.content.value.toByteArray())
                        }.invokeOnCompletion {
                            editorState.saving.value = false
                        }
                    }
                }
            }) {
                Text("save")
            }
            Span({ style { paddingLeft(10.px); paddingRight(10.px) } }) { Text(editorState.file.value?.name ?: "") }
            Text(
                if (editorState.saving.value) {
                    "saving..."
                } else {
                    ""
                }
            )
        }
        TextArea(editorState.content.value) {
            wrap(TextAreaWrap.Off)
            onInput {
                editorState.content.value = it.value
                console.log("change")
            }
            placeholder("content...")
            style {
                display(DisplayStyle.Block)
                width(100.percent)
                height(100.percent)
            }
        }
    }
}

package dev.salavatov.multieditor.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
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
fun EditorPane(editorState: MutableState<EditorState>) {
    val coroutineScope = rememberCoroutineScope()

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
                    val state = editorState.value
                    if (state.file != null && !state.saving) {
                        editorState.value = EditorState(state.file, state.content, true)
                        coroutineScope.launch(Dispatchers.Unconfined) {
                            state.file.write(state.content.toByteArray())
                        }.invokeOnCompletion {
                            editorState.value = EditorState(state.file, state.content, false)
                        }
                    }
                }
            }) {
                Text("save")
            }
            Span({ style { paddingLeft(10.px); paddingRight(10.px) } }) { Text(editorState.value.file?.name ?: "") }
            Text(
                if (editorState.value.saving) {
                    "saving..."
                } else {
                    ""
                }
            )
        }
        TextArea(editorState.value.content) {
            wrap(TextAreaWrap.Off)
            onInput { editorState.value = EditorState(editorState.value.file, it.value) }
            placeholder("content...")
            style {
                display(DisplayStyle.Block)
                width(100.percent)
                height(100.percent)
            }
        }
    }
}

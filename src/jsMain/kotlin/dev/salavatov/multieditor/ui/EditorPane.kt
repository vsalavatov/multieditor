package dev.salavatov.multieditor.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import dev.salavatov.multieditor.state.AppState
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
        Div {
            Button({
                onClick {
                    with(appState.editor) { coroutineScope.launchSaveContent() }
                }
            }) {
                Text("save")
            }
            Span({ style { paddingLeft(10.px); paddingRight(10.px) } }) { Text(editorState.fileState.value?.name ?: "") }
            Text(
                if (editorState.savingState.value) {
                    "saving..."
                } else {
                    ""
                }
            )
        }
        Div({
            style {
                marginTop(10.px)
            }
        }) {
            TextArea(editorState.content) {
                wrap(TextAreaWrap.Off)
                onInput {
                    editorState.content = it.value
                }
                placeholder("content...")
                style {
                    display(DisplayStyle.Block)
                    position(Position.Absolute)
                    width(85.percent)
                    height(93.percent)
                    property("resize", "none")
                    property("border", "none")
                    property("outline", "none")
                    fontSize(1.cssRem)
                }
            }
        }
    }
}

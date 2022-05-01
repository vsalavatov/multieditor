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
    val editorState = remember { appState.editor }

    Div({
        style {
            height(100.percent)
            width(100.percent)
            display(DisplayStyle.InlineBlock)
        }
    }) {
        editorState.fileState.value?.let {
            Div {
                Span({ style { paddingLeft(10.px); paddingRight(60.px) } }) {
                    Text(
                        it.name
                    )
                }
                Button({
                    onClick {
                        appState.launchSaveContent()
                    }
                }) {
                    Text("save")
                }
                Text(
                    if (editorState.savingState.value) {
                        "saving..."
                    } else {
                        ""
                    }
                )
            }
        }
        Div({
            style {
                marginTop(10.px)
                property("border-top", "2px solid gray")
//                border {
//                    width = 1.px
//                    style = LineStyle.Solid
//                    color = Color.gray
//                }
                width(100.percent)
                position(Position.Absolute)
            }
        }) {}
        Div({
            style {
                marginTop(10.px)
                marginLeft(10.px)
            }
        }) {
            TextArea(editorState.content) {
                wrap(TextAreaWrap.Off)
                onInput {
                    editorState.content = it.value
                }
                placeholder("content...")
                style {
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

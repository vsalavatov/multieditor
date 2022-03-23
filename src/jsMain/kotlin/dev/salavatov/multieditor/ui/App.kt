package dev.salavatov.multieditor.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import dev.salavatov.multieditor.state.EditorState
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun App() {
    val editorState = mutableStateOf(EditorState(null, ""))

    Div {
        Span({
            style {
                display(DisplayStyle.InlineBlock)
                maxWidth(30.percent)
                border {
                    width = 3.px
                    style = LineStyle.Solid
                    color = Color.gray
                }
            }
        }) { NavigatorPane(editorState = editorState) }
        Span({
            style {
                display(DisplayStyle.InlineBlock)
            }
        }) { EditorPane(editorState) }
    }

}
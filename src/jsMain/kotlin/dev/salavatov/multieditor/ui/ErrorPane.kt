package dev.salavatov.multieditor.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.salavatov.multieditor.state.AppState
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

object ErrorPaneStyle : StyleSheet() {
    val container by style {
        padding(10.px)
        width(100.percent)
        height(auto)
        backgroundColor(rgb(255, 124, 128))
    }
    val close by style {
        padding(6.px)
        property("float", "right")

        self + before style {
            property("content", "\"⨯\"")
            display(DisplayStyle.InlineBlock)
        }
        self + hover style {
            textDecoration("none")
            cursor("pointer")
        }
    }
}

@Composable
fun ErrorPane(appState: AppState) {
    val error = remember { appState.errorState }
    error.value?.let {
        Div({
            classes(ErrorPaneStyle.container)
        }) {
            Span({ style { fontWeight("bold") } }) {
                Text("Error: ")
            }
            Span {
                var msg = it.toString()
                it.cause?.let {
                    msg += " cause: $it"
                }
                Text(msg)
            }
            Span({
                classes(ErrorPaneStyle.close)
                onClick {
                    error.value = null
                }
            })
        }
    }
}
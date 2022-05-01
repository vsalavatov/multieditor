package dev.salavatov.multieditor.ui

import androidx.compose.runtime.Composable
import dev.salavatov.multieditor.state.AppState
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.dom.Div

object AppStyle : StyleSheet() {
    val main by style {
        fontSize(1.2.em)
    }
}

@Composable
fun AppUI(appState: AppState) {
    Div(attrs = {
        style {
            height(100.percent)
            width(100.percent)
        }
        classes(AppStyle.main)
    }) {
        ErrorPane(appState)
        Div({
            style {
                display(DisplayStyle.InlineBlock)
                height(100.percent)
                maxWidth(30.percent)
                marginRight(10.px)
            }
        }) {
            NavigatorPane(appState)
        }
        Div({
            style {
                display(DisplayStyle.InlineBlock)
                property("border-left", "2px solid gray")
//                border {
//                    width = 2.px
//                    style = LineStyle.Solid
//                    color = Color.gray
//                }
                marginLeft((-2).px)
                height(100.percent)
                position(Position.Absolute)
            }
        }) {}
        Div({
            style {
                display(DisplayStyle.InlineBlock)
                height(100.percent)
                width(auto)
                property("vertical-align", "top")
            }
        }) { EditorPane(appState) }
    }

}
package dev.salavatov.multieditor.ui

import androidx.compose.runtime.*
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span

@OptIn(ExperimentalComposeWebApi::class)
object ExpandableStyle : StyleSheet() {
    val container by style {
        position(Position.Relative)
        height(auto)
        width(100.percent)
    }
    val collapsed by style {
        self + before style {
            property("content", "\"▲\"")
            marginRight(5.px)
            display(DisplayStyle.InlineBlock)
            transform {
                rotate(90.deg)
            }
        }
    }
    val expanded by style {
        self + before style {
            property("content", "\"▲\"")
            marginRight(5.px)
            display(DisplayStyle.InlineBlock)
            transform {
                rotate(180.deg)
            }
        }
    }
}

@Composable
fun Expandable(
    preview: @Composable () -> Unit,
    content: @Composable () -> Unit,
    onExpand: () -> Unit = {},
    onCollapse: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    return Div({
        classes(
            ExpandableStyle.container, if (expanded) {
                ExpandableStyle.expanded
            } else {
                ExpandableStyle.collapsed
            }
        )
    }) {
        Span({
            onClick {
                expanded = if (expanded) {
                    onCollapse()
                    false
                } else {
                    onExpand()
                    true
                }
            }
        }) {
            preview()
        }
        if (expanded) content()
    }
}
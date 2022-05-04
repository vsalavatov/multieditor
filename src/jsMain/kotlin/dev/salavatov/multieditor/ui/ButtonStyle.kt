package dev.salavatov.multieditor.ui

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Span
import org.w3c.dom.HTMLSpanElement

object ButtonStyle : StyleSheet() {
    val button by style {
        border {
            position(Position.Relative)
            display(DisplayStyle.InlineBlock)
            width = 1.px
            style = LineStyle.Solid
            borderRadius(2.px)
            color(Color.darkgray)
        }
        padding(4.px)
        margin(4.px)
    }
}

@Composable
fun SpanButton(onClick: () -> Unit, attrs: AttrBuilderContext<HTMLSpanElement> = {}, content: @Composable () -> Unit) =
    Span({
        onClick { onClick() }
        classes(ButtonStyle.button)
        attrs()
    }) { content() }
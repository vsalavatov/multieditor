package dev.salavatov.multieditor.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.keywords.CSSAutoKeyword
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Image
import kotlin.math.exp

@Composable
fun Expandable(preview: @Composable () -> Unit, content: @Composable () -> Unit) {
    val expanded = remember { mutableStateOf(false) }
    return Div({
        style {
            height(auto)
            width(100.percent)
        }
    }) {
        Span({
            onClick {
                expanded.value = expanded.value.xor(true)
            }
            if (!expanded.value) {
                style {
                    color(Color.darkgray)
                }
            }
        }) {
            preview()
        }
        if (expanded.value) content()
    }
    /*(
    {
        Row(modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
            Icon(if (expanded.value) {
                Icons.Default.KeyboardArrowDown
            } else {
                Icons.Default.KeyboardArrowRight
            }, contentDescription = null, tint = LocalContentColor.current)
            preview()
        }
        if (expanded.value) {
            content()
        }
    }*/
}
package dev.salavatov.multieditor.ui

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

// https://www.w3schools.com/howto/howto_css_modals.asp
object ModalDialogStyle : StyleSheet() {
    val modal by style {
        display(DisplayStyle.Block)
        position(Position.Fixed)
        property("z-index", 1)
        property("left", 0)
        property("top", 0)
        width(100.percent)
        height(100.percent)
        property("overflow", "auto")
        backgroundColor(rgba(0, 0, 0, 0.5))
    }

    val modalContent by style {
        backgroundColor(Color.white)
        property("margin", "25% auto")
        padding(20.px)
        border {
            width = 1.px
            style = LineStyle.Solid
            color = Color.darkgray
        }
        width(80.percent)
    }

    val modalHeader by style {
        padding(2.px, 16.px)
        backgroundColor(Color.lightgray)
        color(Color.black)
    }

    val modalBody by style {
        padding(2.px, 16.px)
    }

    val close by style {
        color(Color.black)
        property("float", "right")
        fontSize(28.px)
        fontWeight("bold")

        self + hover style {
            color(Color.white)
            textDecoration("none")
            cursor("pointer")
        }
        self + focus style {
            color(Color.white)
            textDecoration("none")
            cursor("pointer")
        }
    }
}

@Composable
fun ModalDialog(
    onDismissRequest: () -> Unit,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit
) = Div({
    classes(ModalDialogStyle.modal)
}) {
    Div({
        classes(ModalDialogStyle.modalContent)
    }) {

        Div({
            classes(ModalDialogStyle.modalHeader)
        }) {
            Span({
                classes(ModalDialogStyle.close)
                onClick { onDismissRequest() }
            }) { Text("×") }
            H2 { title() }
        }

        Div({
            classes(ModalDialogStyle.modalBody)
        }) {
            content()
        }
    }
}
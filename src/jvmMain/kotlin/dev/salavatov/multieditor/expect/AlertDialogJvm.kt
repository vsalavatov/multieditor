package dev.salavatov.multieditor.expect

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState

@Composable
internal actual fun ActualAlert(
    onDismissRequest: () -> Unit,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) = Dialog(
    onDismissRequest,
    state = rememberDialogState(),
    title = title,
    undecorated = true,
    content = {
        AlertCard {
            WindowDraggableArea(modifier = Modifier.wrapContentHeight()) {
                Headline(title, onDismissRequest)
            }
            content()
        }
    })
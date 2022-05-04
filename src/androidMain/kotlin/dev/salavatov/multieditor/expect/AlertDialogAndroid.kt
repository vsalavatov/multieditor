package dev.salavatov.multieditor.expect

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
internal actual fun ActualAlert(
    onDismissRequest: () -> Unit,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) = Dialog(onDismissRequest, content = {
    AlertCard {
        Headline(title, onDismissRequest)
        content()
    }
})
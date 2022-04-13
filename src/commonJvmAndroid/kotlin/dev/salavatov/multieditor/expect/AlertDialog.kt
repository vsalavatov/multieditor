package dev.salavatov.multieditor.expect

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AlertDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    buttons: @Composable () -> Unit,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
) = ActualAlert(
    modifier, onDismissRequest, buttons, title, content
)

@Composable
internal expect fun ActualAlert(
    modifier: Modifier,
    onDismissRequest: () -> Unit,
    buttons: @Composable () -> Unit,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
)
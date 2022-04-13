package dev.salavatov.multieditor.expect

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun ActualAlert(
    modifier: Modifier,
    onDismissRequest: () -> Unit,
    buttons: @Composable () -> Unit,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
) = androidx.compose.material.AlertDialog(onDismissRequest, buttons, modifier, title, content)
package dev.salavatov.multieditor.expect

import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal actual fun ActualAlert(
    modifier: Modifier,
    onDismissRequest: () -> Unit,
    buttons: @Composable () -> Unit,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
) = AlertDialog(onDismissRequest, buttons, modifier, title, content)
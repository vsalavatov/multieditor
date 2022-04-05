package dev.salavatov.multieditor.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun Expandable(preview: @Composable () -> Unit, content: @Composable () -> Unit) {
    val expanded = remember { mutableStateOf(false) }
    return Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable {
                expanded.value = expanded.value.xor(true)
            }
    ) {
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
    }
}
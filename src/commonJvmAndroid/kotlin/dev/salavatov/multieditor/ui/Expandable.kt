package dev.salavatov.multieditor.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun Expandable(
    preview: @Composable () -> Unit,
    content: @Composable () -> Unit,
    onExpand: () -> Unit = {},
    onCollapse: () -> Unit = {}
) {
    val expanded = remember { mutableStateOf(false) }
    return Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .clickable {
                    if (expanded.value) {
                        onCollapse()
                        expanded.value = false
                    } else {
                        onExpand()
                        expanded.value = true
                    }
                }
        )
        {
            Icon(
                if (expanded.value) {
                    Icons.Default.KeyboardArrowDown
                } else {
                    Icons.Default.KeyboardArrowRight
                }, contentDescription = null, tint = LocalContentColor.current
            )
            preview()
        }
        if (expanded.value) {
            content()
        }
    }
}
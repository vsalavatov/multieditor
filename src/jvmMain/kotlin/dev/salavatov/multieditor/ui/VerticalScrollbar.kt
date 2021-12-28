// source: https://github.com/JetBrains/compose-jb/blob/master/examples/codeviewer/common/src/desktopMain/kotlin/org/jetbrains/codeviewer/platform/Scrollbar.kt
package dev.salavatov.multieditor.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun VerticalScrollbar(
    modifier: Modifier,
    scrollState: ScrollState
)= androidx.compose.foundation.VerticalScrollbar(
    rememberScrollbarAdapter(scrollState),
    modifier
)

@Composable
fun VerticalScrollbar(
    modifier: Modifier,
    scrollState: LazyListState
) = androidx.compose.foundation.VerticalScrollbar(
    rememberScrollbarAdapter(scrollState),
    modifier
)
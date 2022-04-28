package dev.salavatov.multieditor.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import dev.salavatov.multieditor.NamedStorageFactory

class AppState(
    val editor: EditorState,
    val navigation: NavigationState
) {
}

@Composable
fun makeStartAppState(storages: List<NamedStorageFactory>): AppState {
    return remember {
        AppState(
            EditorState(
                mutableStateOf(null),
                mutableStateOf(""),
                mutableStateOf(false)
            ),
            NavigationState(
                mutableStateOf(storages)
            )
        )
    }
}
package dev.salavatov.multieditor.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import dev.salavatov.multieditor.NamedStorageFactory

class NavigationState(
    val availableStoragesState: MutableState<List<NamedStorageFactory>>
) {
    var availableStorages by availableStoragesState
}
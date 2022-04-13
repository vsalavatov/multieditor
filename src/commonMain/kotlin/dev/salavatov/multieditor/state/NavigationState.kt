package dev.salavatov.multieditor.state

import androidx.compose.runtime.MutableState
import dev.salavatov.multieditor.NamedStorage
import dev.salavatov.multieditor.NamedStorageFactory

class NavigationState(
    val configuredStorages: MutableState<List<NamedStorage>>,
    val availableStorages: MutableState<List<NamedStorageFactory>>
)
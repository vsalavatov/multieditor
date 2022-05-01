package dev.salavatov.multieditor.state

import androidx.compose.runtime.snapshots.SnapshotStateList
import dev.salavatov.multieditor.NamedStorageFactory

class NavigationState(
    val availableStoragesState: SnapshotStateList<NamedStorageFactory>,
    val configuredStoragesState: SnapshotStateList<FileTree>
)
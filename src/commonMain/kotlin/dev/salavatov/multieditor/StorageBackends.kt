package dev.salavatov.multieditor

import androidx.compose.runtime.Composable
import dev.salavatov.multifs.vfs.GenericFS

data class NamedStorageFactory(val name: String, val init: suspend () -> GenericFS)
data class NamedStorage(val name: String, val storage: GenericFS)
package dev.salavatov.multieditor

import dev.salavatov.multifs.cloud.googledrive.*
import dev.salavatov.multifs.vfs.GenericFS

data class StorageFactory(val name: String, val init: suspend () -> GenericFS)

expect object StorageBackends {
    val backends: List<StorageFactory>
}
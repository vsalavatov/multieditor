package dev.salavatov.multieditor

import dev.salavatov.multifs.vfs.GenericFS

typealias Storage = GenericFS

data class NamedStorageFactory(val name: String, val init: suspend () -> Storage)

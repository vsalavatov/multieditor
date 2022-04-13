package dev.salavatov.multieditor

import dev.salavatov.multifs.vfs.GenericFS

data class NamedStorageFactory(val name: String, val init: suspend () -> GenericFS)
data class NamedStorage(val name: String, val storage: GenericFS)

expect class StorageConfig

expect fun makeStorageList(config: StorageConfig): List<NamedStorageFactory>
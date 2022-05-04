package dev.salavatov.multieditor

import dev.salavatov.multifs.vfs.File
import dev.salavatov.multifs.vfs.Folder
import dev.salavatov.multifs.vfs.VFS


typealias Storage = VFS<out File, out Folder>

class NamedStorageFactory(val name: String, val init: suspend () -> Storage)

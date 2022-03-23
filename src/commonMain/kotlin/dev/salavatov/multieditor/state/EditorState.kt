package dev.salavatov.multieditor.state

import dev.salavatov.multifs.vfs.File

class EditorState(val file: File?, val content: String, val saving: Boolean = false)
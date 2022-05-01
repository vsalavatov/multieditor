package dev.salavatov.multieditor.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import dev.salavatov.multieditor.Storage
import dev.salavatov.multieditor.util.unreachable
import dev.salavatov.multifs.vfs.File
import dev.salavatov.multifs.vfs.Folder
import dev.salavatov.multifs.vfs.VFSNode

sealed class FileTreeNode

class FileNode(
    val file: File,
    val parent: FolderNode?,
    val changed: MutableState<Boolean>
) : FileTreeNode() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is FileNode) {
            return file == other.file && parent == other.parent
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = file.hashCode()
        result = 31 * result + (parent?.hashCode() ?: 0)
        return result
    }
}

class FolderNode(
    val folder: Folder,
    val parent: FolderNode?,
    val children: SnapshotStateList<FileTreeNode>
) : FileTreeNode() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is FolderNode) {
            return folder == other.folder && parent == other.parent
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = folder.hashCode()
        result = 31 * result + (parent?.hashCode() ?: 0)
        return result
    }
}

class FileTree(
    val fs: Storage,
    val name: String,
    val root: FolderNode
)

fun VFSNode.toFileTreeNode(parent: FolderNode?): FileTreeNode = when (this) {
    is File -> FileNode(this, parent, mutableStateOf(false))
    is Folder -> FolderNode(this, parent, mutableStateListOf())
    else -> unreachable("non-exhaustive VFSNode conversion")
}
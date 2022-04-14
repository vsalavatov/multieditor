import androidx.compose.runtime.remember
import dev.salavatov.multieditor.makeStorageList
import dev.salavatov.multieditor.state.makeStartState
import dev.salavatov.multieditor.ui.AppUI
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        val storages = remember { makeStorageList() }
        val appState = makeStartState(storages)
        AppUI(appState)
    }
}
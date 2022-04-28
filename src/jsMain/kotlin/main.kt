import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.salavatov.multieditor.makeStorageList
import dev.salavatov.multieditor.state.makeStartAppState
import dev.salavatov.multieditor.ui.*
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable

@Composable
fun mountStyles() {
    Style(AppStyle)
    Style(ExpandableStyle)
    Style(NavigatorPaneStyle)
    Style(ModalDialogStyle)
    Style(ButtonStyle)
    Style(NodeActions)
    Style(ErrorPaneStyle)
}

fun main() {
    renderComposable(rootElementId = "root") {
        mountStyles()

        val storages = remember { makeStorageList() }
        val appState = makeStartAppState(storages)

        AppUI(appState)
    }
}
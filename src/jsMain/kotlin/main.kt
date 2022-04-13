import dev.salavatov.multieditor.ui.AppUI
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        AppUI()
    }
}
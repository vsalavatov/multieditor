package dev.salavatov.multieditor

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.salavatov.multieditor.ui.*

@Composable
fun App() {
    val editorState = mutableStateOf(EditorState(null, ""))

    MaterialTheme(colors = AppTheme.colors.material, typography = Typography(AppFonts.robotoMono())) {
        Row {
            NavigatorPane(modifier = Modifier.fillMaxWidth(0.3f).fillMaxHeight(), editorState = editorState)
            Divider(modifier = Modifier.fillMaxHeight().width(1.dp), color = Color.LightGray, thickness = 1.dp)
            EditorPane(editorState, modifier = Modifier.fillMaxWidth().fillMaxHeight())
        }
    }
}

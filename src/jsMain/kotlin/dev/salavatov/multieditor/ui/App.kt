package dev.salavatov.multieditor.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import dev.salavatov.multieditor.state.EditorState
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun App() {
    val editorState = mutableStateOf(EditorState(null, ""))

    Div {
        NavigatorPane(editorState = editorState)
//        Divider(modifier = Modifier.fillMaxHeight().width(1.dp), color = Color.LightGray, thickness = 1.dp)
//        EditorPane(editorState, modifier = Modifier.fillMaxWidth().fillMaxHeight())
    }

}
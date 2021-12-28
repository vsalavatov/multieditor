package dev.salavatov.multieditor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import dev.salavatov.multieditor.multifs.CacheGoogleAuthenticator
import dev.salavatov.multieditor.ui.*
import dev.salavatov.multifs.cloud.googledrive.*
import dev.salavatov.multifs.systemfs.SystemFS
import dev.salavatov.multifs.systemfs.SystemFS.Companion.represent
import dev.salavatov.multifs.vfs.VFSException
import kotlinx.coroutines.*
import java.io.File

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

package dev.salavatov.multieditor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.salavatov.multieditor.state.AppState

@Composable
fun AppUI(state: AppState) {
    MaterialTheme(colors = AppTheme.colors.material, typography = Typography(AppFonts.robotoMono())) {
        Column(modifier = Modifier.fillMaxSize()) {
            ErrorPane(state)
            Row {
                NavigatorPane(state, modifier = Modifier.fillMaxWidth(0.3f).fillMaxHeight())
                Divider(modifier = Modifier.fillMaxHeight().width(1.dp), color = Color.LightGray, thickness = 1.dp)
                EditorPane(state, modifier = Modifier.fillMaxWidth().fillMaxHeight())
            }
        }
    }
}

package dev.salavatov.multieditor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIconDefaults
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import dev.salavatov.multieditor.state.AppState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ErrorPane(appState: AppState) {
    val error = remember { appState.errorState }
    error.value?.let {
        Row(
            modifier = Modifier
                .fillMaxWidth(1.0f)
                .wrapContentHeight()
                .background(Color(255, 124, 128))
        ) {
            Row(modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Error: ", fontWeight = FontWeight.Bold)
                Text(it.toString())
            }
            Text(
                "x",
                fontSize = 1.5.em,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                    .padding(6.dp)
                    .pointerHoverIcon(PointerIconDefaults.Hand)
                    .clickable {
                        error.value = null
                    }
            )
        }
    }
}
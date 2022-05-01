package dev.salavatov.multieditor.expect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) = ActualAlert(
    onDismissRequest, title, content
)


@Composable
internal expect fun ActualAlert(
    onDismissRequest: () -> Unit,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
)

@Composable
fun AlertCard(content: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxSize()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
                content()
            }
        }
    }
}

@Composable
fun Headline(title: String, onDismissRequest: () -> Unit) {
    Column(
        modifier = Modifier.wrapContentWidth().wrapContentHeight(align = Alignment.Top),
        verticalArrangement = Arrangement.Top
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(title, modifier = Modifier.padding(2.dp), fontWeight = FontWeight.Bold, textAlign = TextAlign.Start)
            Text(
                "X",
                modifier = Modifier.padding(2.dp, 2.dp, 4.dp, 2.dp).clickable { onDismissRequest() },
                textAlign = TextAlign.End
            )
        }
        Divider(modifier = Modifier.fillMaxWidth())
    }
}

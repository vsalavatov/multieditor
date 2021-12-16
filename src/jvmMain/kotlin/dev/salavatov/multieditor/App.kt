package dev.salavatov.multieditor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.salavatov.multieditor.ui.VerticalScrollbar
import dev.salavatov.multifs.cloud.googledrive.*
import kotlinx.coroutines.launch

@Composable
fun App() {
    val coroutineScope = rememberCoroutineScope()
    var tokens by remember { mutableStateOf("") }
    val gauth = CacheGoogleAuthenticator(
        GoogleAppCredentials(
            "783177635948-ishda9322n9pk96b2uc6opp729ia0a42.apps.googleusercontent.com",
            "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"
        )
    )
    val gapi = GoogleDriveAPI(gauth)
    val gdfs = GoogleDriveFS(gapi)

    var currentFolder: GDriveFolder? by mutableStateOf(null)
    var fileList: List<GDriveNode> by mutableStateOf(emptyList())
    var selectedFile: GDriveFile? by mutableStateOf(null)

    var fileContents = remember{ mutableStateOf(TextFieldValue("<contents>")) }

    MaterialTheme {
        Row {
            Column(Modifier.padding(8.dp).fillMaxWidth(0.4f)) {
                Button(onClick = {
                    coroutineScope.launch {
                        currentFolder = gdfs.root
                        fileList = currentFolder!!.listFolder()
                    }
                }) {
                    Text("Login")
                }

                val monospace = FontFamily.Monospace
                @Composable
                fun makeText(text: String, modifier: Modifier = Modifier) = Text(text, fontFamily = monospace, letterSpacing = 0.sp, modifier = modifier)

                Row(modifier = Modifier.padding(50.dp)) {
                    makeText(currentFolder?.name ?: "<name>")
                    Spacer(modifier = Modifier.width(30.dp))
                    makeText(currentFolder?.id ?: "<id>", modifier = Modifier.width(50.dp))
                    Spacer(modifier = Modifier.width(30.dp))
                    Button({
                        coroutineScope.launch {
                            currentFolder = currentFolder?.parent
                            fileList = currentFolder?.listFolder() ?: emptyList()
                        }
                    }) { makeText("GO UP") }
                    Spacer(modifier = Modifier.width(30.dp))
                    Column {
                        var fileName = remember{ mutableStateOf(TextFieldValue("folder name")) }
                        TextField(fileName.value, { fileName.value = it })
                        Row {
                            Button({
                                coroutineScope.launch {
                                    currentFolder?.createFolder(fileName.value.text)
                                    fileList = currentFolder?.listFolder() ?: emptyList()
                                }
                            }) {
                                makeText("create folder")
                            }
                            Button({
                                coroutineScope.launch {
                                    currentFolder?.createFile(fileName.value.text)
                                    fileList = currentFolder?.listFolder() ?: emptyList()
                                }
                            }) {
                                makeText("create file")
                            }
                        }
                    }
                }

                Box {
                    // useful: https://github.com/JetBrains/compose-jb/blob/master/examples/codeviewer/common/src/commonMain/kotlin/org/jetbrains/codeviewer/ui/filetree/FileTreeView.kt
                    val scrollState = rememberLazyListState()

                    LazyColumn {
                        items(fileList.size) { index ->
                            when (val file = fileList[index]) {
                                is GDriveFolder -> {
                                    Row(modifier = Modifier.clickable {
                                        coroutineScope.launch {
                                            currentFolder = file
                                            fileList = currentFolder!!.listFolder()
                                        }
                                    }) {
                                        makeText("FOLDER")
                                        Spacer(modifier = Modifier.width(30.dp))
                                        makeText(file.name)
                                        Spacer(modifier = Modifier.width(30.dp))
                                        makeText(file.id)
                                    }
                                }
                                is GDriveFile -> {
                                    Row(modifier = Modifier.clickable {
                                        coroutineScope.launch {
                                            selectedFile = file
                                            fileContents.value = TextFieldValue(String(selectedFile!!.read()))
                                        }
                                    }) {
                                        makeText("FILE")
                                        Spacer(modifier = Modifier.width(30.dp))
                                        makeText(file.name)
                                        Spacer(modifier = Modifier.width(30.dp))
                                        makeText(file.id)
                                        Spacer(modifier = Modifier.width(30.dp))
                                        makeText(file.mimeType)
                                        Spacer(modifier = Modifier.width(30.dp))
                                        makeText(file.size.toString())
                                    }
                                }
                            }
                        }
                    }

                    VerticalScrollbar(Modifier.align(Alignment.CenterEnd), scrollState)
                }
            }
            Column(modifier = Modifier.fillMaxWidth(1f).then(Modifier.fillMaxHeight(1f))) {
                Row {
                    Button({
                        coroutineScope.launch {
                            selectedFile?.write(fileContents.value.text.toByteArray())
                        }
                    }) {
                        Text("save")
                    }
                    Text(selectedFile?.name.toString())
                }
                TextField(fileContents.value, { fileContents.value = it })
            }

        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
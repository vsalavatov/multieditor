package dev.salavatov.multieditor.ui


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import java.io.File

object AppFonts {
    private fun fromResource(path: String): File = File(
        AppFonts::class.java.classLoader.getResource(path)?.toURI()
            ?: error("font not found")
    )

    @Composable
    fun robotoMono() = remember {
        FontFamily(
            Font(
                fromResource("roboto_mono/RobotoMono-Regular.ttf"),
                FontWeight.Normal,
                FontStyle.Normal
            ),
            Font(
                fromResource("roboto_mono/RobotoMono-Italic.ttf"),
                FontWeight.Normal,
                FontStyle.Italic
            ),
            Font(
                fromResource("roboto_mono/RobotoMono-Bold.ttf"),
                FontWeight.Bold,
                FontStyle.Normal
            ),
            Font(
                fromResource("roboto_mono/RobotoMono-BoldItalic.ttf"),
                FontWeight.Bold,
                FontStyle.Italic
            ),
            Font(
                fromResource("roboto_mono/RobotoMono-Light.ttf"),
                FontWeight.Light,
                FontStyle.Normal
            ),
            Font(
                fromResource("roboto_mono/RobotoMono-LightItalic.ttf"),
                FontWeight.Light,
                FontStyle.Italic
            ),
            Font(
                fromResource("roboto_mono/RobotoMono-Medium.ttf"),
                FontWeight.Medium,
                FontStyle.Normal
            ),
            Font(
                fromResource("roboto_mono/RobotoMono-MediumItalic.ttf"),
                FontWeight.Medium,
                FontStyle.Italic
            ),
            Font(
                fromResource("roboto_mono/RobotoMono-Thin.ttf"),
                FontWeight.Thin,
                FontStyle.Normal
            ),
            Font(
                fromResource("roboto_mono/RobotoMono-ThinItalic.ttf"),
                FontWeight.Thin,
                FontStyle.Italic
            ),
        )
    }
}
package dev.salavatov.multieditor.ui


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import java.io.File

actual object AppFonts {
    private fun fromResource(path: String): File = File(
        AppFonts::class.java.classLoader!!.getResource(path)?.toURI()
            ?: error("font not found")
    )

    @Composable
    actual fun robotoMono() = remember {
        FontFamily(
            Font(
                fromResource("font/roboto_mono_regular.ttf"),
                FontWeight.Normal,
                FontStyle.Normal
            ),
            Font(
                fromResource("font/roboto_mono_italic.ttf"),
                FontWeight.Normal,
                FontStyle.Italic
            ),
            Font(
                fromResource("font/roboto_mono_bold.ttf"),
                FontWeight.Bold,
                FontStyle.Normal
            ),
            Font(
                fromResource("font/roboto_mono_bold_italic.ttf"),
                FontWeight.Bold,
                FontStyle.Italic
            ),
            Font(
                fromResource("font/roboto_mono_light.ttf"),
                FontWeight.Light,
                FontStyle.Normal
            ),
            Font(
                fromResource("font/roboto_mono_light_italic.ttf"),
                FontWeight.Light,
                FontStyle.Italic
            ),
            Font(
                fromResource("font/roboto_mono_medium.ttf"),
                FontWeight.Medium,
                FontStyle.Normal
            ),
            Font(
                fromResource("font/roboto_mono_medium_italic.ttf"),
                FontWeight.Medium,
                FontStyle.Italic
            ),
            Font(
                fromResource("font/roboto_mono_thin.ttf"),
                FontWeight.Thin,
                FontStyle.Normal
            ),
            Font(
                fromResource("font/roboto_mono_thin_italic.ttf"),
                FontWeight.Thin,
                FontStyle.Italic
            ),
        )
    }
}
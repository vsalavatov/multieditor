package dev.salavatov.multieditor.ui


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import dev.salavatov.multieditor.R

actual object AppFonts {
    @Composable
    actual fun robotoMono() = remember {
        FontFamily(
            Font(
                R.font.roboto_mono_regular,
                FontWeight.Normal,
                FontStyle.Normal
            ),
            Font(
                R.font.roboto_mono_italic,
                FontWeight.Normal,
                FontStyle.Italic
            ),
            Font(
                R.font.roboto_mono_bold,
                FontWeight.Bold,
                FontStyle.Normal
            ),
            Font(
                R.font.roboto_mono_bold_italic,
                FontWeight.Bold,
                FontStyle.Italic
            ),
            Font(
                R.font.roboto_mono_light,
                FontWeight.Light,
                FontStyle.Normal
            ),
            Font(
                R.font.roboto_mono_light_italic,
                FontWeight.Light,
                FontStyle.Italic
            ),
            Font(
                R.font.roboto_mono_medium,
                FontWeight.Medium,
                FontStyle.Normal
            ),
            Font(
                R.font.roboto_mono_medium_italic,
                FontWeight.Medium,
                FontStyle.Italic
            ),
            Font(
                R.font.roboto_mono_thin,
                FontWeight.Thin,
                FontStyle.Normal
            ),
            Font(
                R.font.roboto_mono_thin_italic,
                FontWeight.Thin,
                FontStyle.Italic
            ),
        )
    }
}
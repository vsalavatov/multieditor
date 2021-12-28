// source: https://github.com/JetBrains/compose-jb/blob/1f15bbb373c84c93245393fd9db22bbc36e911ab/examples/codeviewer/common/src/commonMain/kotlin/org/jetbrains/codeviewer/ui/common/Fonts.kt

package dev.salavatov.multieditor.ui

import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle

object AppTheme {
    val colors: Colors = Colors()

    val code: Code = Code()

    class Colors(
        val backgroundDark: Color = Color(0xFF2B2B2B),
        val backgroundMedium: Color = Color(0xFF3C3F41),
        val backgroundLight: Color = Color(0xFF4E5254),

        val material: androidx.compose.material.Colors =
            darkColors(
                background = backgroundDark,
                surface = backgroundMedium,
                primary = Color.White,
                primaryVariant = Color.LightGray,
                secondary = Color(0xFF22EE22)
            ),
    )

    class Code(
        val simple: SpanStyle = SpanStyle(Color(0xFFA9B7C6)),
        val value: SpanStyle = SpanStyle(Color(0xFF6897BB)),
        val keyword: SpanStyle = SpanStyle(Color(0xFFCC7832)),
        val punctuation: SpanStyle = SpanStyle(Color(0xFFA1C17E)),
        val annotation: SpanStyle = SpanStyle(Color(0xFFBBB529)),
        val comment: SpanStyle = SpanStyle(Color(0xFF808080))
    )
}
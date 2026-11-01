package site.yatharthwebs.ampere.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Default skin, defined in code for Phase 0.
 * Moves to a bundled skin pack (assets) in P3-04. Values match docs/UI_SPEC.md section 6.2.
 */
val ClassicLcdSkin =
    Skin(
        id = "classic-lcd",
        name = "Classic LCD",
        dark = true,
        colors =
            SkinColors(
                background = Color(0xFF0F1115),
                surface = Color(0xFF171A21),
                surfaceVariant = Color(0xFF1F232C),
                onSurface = Color(0xFFE6E8EE),
                onSurfaceMuted = Color(0xFF9AA1B2),
                accent = Color(0xFF5EE07A),
                onAccent = Color(0xFF06210F),
                outline = Color(0xFF2B3040),
                danger = Color(0xFFFF5C5C),
                lcdBackground = Color(0xFF0D1410),
                lcdForeground = Color(0xFF5EE07A),
                lcdDim = Color(0xFF3F9E55),
                analyzerLow = Color(0xFF5EE07A),
                analyzerMid = Color(0xFFC9E85E),
                analyzerHigh = Color(0xFFFF7A5C),
                analyzerPeak = Color(0xFFFFFFFF),
                selectionBg = Color(0xFF1E3A28),
            ),
        shapes =
            SkinShapes(
                cornerSmall = 4.dp,
                cornerMedium = 8.dp,
                cornerLarge = 16.dp,
                outlineWidth = 1.dp,
            ),
        typography =
            SkinTypography(
                fontUi = FontFamily.Default,
                fontLcd = FontFamily.Monospace, // custom LCD font later; log its license in docs/ASSETS.md
                lcdLarge = 36.sp,
                lcdBody = 14.sp,
            ),
        controls =
            SkinControls(
                transportStyle = TransportStyle.Outline,
                sliderStyle = SliderStyle.Block,
                analyzerStyle = AnalyzerStyle.Segmented,
                barCount = 24,
                peakHold = true,
                mergePlayPause = false,
            ),
    )

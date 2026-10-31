package site.yatharthwebs.ampere.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

/** Design tokens for a skin. Mirrors docs/UI_SPEC.md section 5. */
@Immutable
data class SkinColors(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val onSurface: Color,
    val onSurfaceMuted: Color,
    val accent: Color,
    val onAccent: Color,
    val outline: Color,
    val danger: Color,
    val lcdBackground: Color,
    val lcdForeground: Color,
    val lcdDim: Color,
    val analyzerLow: Color,
    val analyzerMid: Color,
    val analyzerHigh: Color,
    val analyzerPeak: Color,
    val selectionBg: Color,
)

@Immutable
data class SkinShapes(
    val cornerSmall: Dp,
    val cornerMedium: Dp,
    val cornerLarge: Dp,
    val outlineWidth: Dp,
)

@Immutable
data class SkinTypography(
    val fontUi: FontFamily,
    val fontLcd: FontFamily,
    val lcdLarge: TextUnit,
    val lcdBody: TextUnit,
)

enum class TransportStyle { Outline, Filled, Sprite }

enum class SliderStyle { Line, Block, Sprite }

enum class AnalyzerStyle { Bars, Segmented, Line, Dots }

@Immutable
data class SkinControls(
    val transportStyle: TransportStyle,
    val sliderStyle: SliderStyle,
    val analyzerStyle: AnalyzerStyle,
    val barCount: Int,
    val peakHold: Boolean,
    val mergePlayPause: Boolean,
)

@Immutable
data class Skin(
    val id: String,
    val name: String,
    val dark: Boolean,
    val colors: SkinColors,
    val shapes: SkinShapes,
    val typography: SkinTypography,
    val controls: SkinControls,
)

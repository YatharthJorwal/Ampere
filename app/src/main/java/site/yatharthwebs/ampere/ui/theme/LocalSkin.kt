package site.yatharthwebs.ampere.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf

/** The active skin. Components read colors, shapes and fonts only from here. */
val LocalSkin = staticCompositionLocalOf { ClassicLcdSkin }

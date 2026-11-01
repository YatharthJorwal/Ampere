package site.yatharthwebs.ampere.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AmpereTheme(
    skin: Skin = ClassicLcdSkin,
    content: @Composable () -> Unit,
) {
    val c = skin.colors
    val colorScheme =
        if (skin.dark) {
            darkColorScheme(
                primary = c.accent,
                onPrimary = c.onAccent,
                background = c.background,
                onBackground = c.onSurface,
                surface = c.surface,
                onSurface = c.onSurface,
                surfaceVariant = c.surfaceVariant,
                onSurfaceVariant = c.onSurfaceMuted,
                outline = c.outline,
                error = c.danger,
                secondaryContainer = c.selectionBg,
                onSecondaryContainer = c.onSurface,
            )
        } else {
            lightColorScheme(
                primary = c.accent,
                onPrimary = c.onAccent,
                background = c.background,
                onBackground = c.onSurface,
                surface = c.surface,
                onSurface = c.onSurface,
                surfaceVariant = c.surfaceVariant,
                onSurfaceVariant = c.onSurfaceMuted,
                outline = c.outline,
                error = c.danger,
                secondaryContainer = c.selectionBg,
                onSecondaryContainer = c.onSurface,
            )
        }
    val shapes =
        Shapes(
            small = RoundedCornerShape(skin.shapes.cornerSmall),
            medium = RoundedCornerShape(skin.shapes.cornerMedium),
            large = RoundedCornerShape(skin.shapes.cornerLarge),
        )
    CompositionLocalProvider(LocalSkin provides skin) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = shapes,
            content = content,
        )
    }
}

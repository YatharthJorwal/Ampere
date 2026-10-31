package site.yatharthwebs.ampere.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import site.yatharthwebs.ampere.R
import site.yatharthwebs.ampere.ui.theme.AmpereTheme
import site.yatharthwebs.ampere.ui.theme.LocalSkin

/** Static stand-in for the mini player. Real playback state arrives in P1-05. */
@Composable
fun MiniPlayerPlaceholder(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val skin = LocalSkin.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(skin.colors.surfaceVariant)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.mini_player_idle),
            color = skin.colors.onSurfaceMuted,
            fontFamily = skin.typography.fontLcd,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MiniPlayerPlaceholderPreview() {
    AmpereTheme { MiniPlayerPlaceholder(onClick = {}) }
}

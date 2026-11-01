package site.yatharthwebs.ampere.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import site.yatharthwebs.ampere.ui.theme.AmpereTheme
import site.yatharthwebs.ampere.ui.theme.LocalSkin

/** LCD-style readout: time, title, technical line. Analyzer area is added in P2-06. */
@Composable
fun LcdPanel(
    timeText: String,
    title: String,
    techLine: String,
    modifier: Modifier = Modifier,
) {
    val skin = LocalSkin.current
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(skin.shapes.cornerMedium))
                .background(skin.colors.lcdBackground)
                .padding(16.dp),
    ) {
        Text(
            text = timeText,
            color = skin.colors.lcdForeground,
            fontFamily = skin.typography.fontLcd,
            fontSize = skin.typography.lcdLarge,
        )
        Text(
            text = title,
            color = skin.colors.lcdForeground,
            fontFamily = skin.typography.fontLcd,
            fontSize = skin.typography.lcdBody,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = techLine,
            color = skin.colors.lcdDim,
            fontFamily = skin.typography.fontLcd,
            fontSize = skin.typography.lcdBody,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LcdPanelPreview() {
    AmpereTheme {
        LcdPanel(
            timeText = "01:58",
            title = "3. Blue Monday - New Order",
            techLine = "320 kbps 44 kHz stereo",
            modifier = Modifier.padding(16.dp),
        )
    }
}

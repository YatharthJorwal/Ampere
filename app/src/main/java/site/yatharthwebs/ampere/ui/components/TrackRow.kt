package site.yatharthwebs.ampere.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import site.yatharthwebs.ampere.R
import site.yatharthwebs.ampere.domain.DurationFormat
import site.yatharthwebs.ampere.domain.Track
import site.yatharthwebs.ampere.ui.theme.AmpereTheme
import site.yatharthwebs.ampere.ui.theme.LocalSkin

/** One song in a list: title, artist and album, and duration in the LCD font. */
@Composable
fun TrackRow(
    track: Track,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val skin = LocalSkin.current
    val artist = track.artist.ifEmpty { stringResource(R.string.unknown_artist) }
    val subtitle = if (track.album.isEmpty()) artist else "$artist · ${track.album}"
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .heightIn(min = 56.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.title,
                style = MaterialTheme.typography.bodyLarge,
                color = skin.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = skin.colors.onSurfaceMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(
            text = DurationFormat.format(track.durationMs),
            color = skin.colors.lcdDim,
            fontFamily = skin.typography.fontLcd,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackRowPreview() {
    AmpereTheme {
        TrackRow(
            track =
                Track(
                    id = 1L,
                    uri = "content://media/1",
                    title = "Blue Monday",
                    artist = "New Order",
                    album = "Power, Corruption & Lies",
                    durationMs = 449_000L,
                    trackNumber = 1,
                    dateAddedSec = 0L,
                    mimeType = "audio/mpeg",
                ),
            onClick = {},
        )
    }
}

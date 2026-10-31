package site.yatharthwebs.ampere.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import site.yatharthwebs.ampere.R
import site.yatharthwebs.ampere.ui.components.LcdPanel
import site.yatharthwebs.ampere.ui.theme.AmpereTheme

@Composable
private fun PlaceholderScreen(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun LibraryScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(
        title = stringResource(R.string.library_title),
        body = stringResource(R.string.library_body),
        modifier = modifier,
    )
}

@Composable
fun PlaylistScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(
        title = stringResource(R.string.playlist_title),
        body = stringResource(R.string.playlist_body),
        modifier = modifier,
    )
}

@Composable
fun NowPlayingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        LcdPanel(
            timeText = stringResource(R.string.lcd_time_zero),
            title = stringResource(R.string.lcd_no_track),
            techLine = stringResource(R.string.lcd_tech_none),
        )
        Text(
            text = stringResource(R.string.now_playing_body),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(
        title = stringResource(R.string.settings_title),
        body = stringResource(R.string.about_body),
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun NowPlayingScreenPreview() {
    AmpereTheme { NowPlayingScreen() }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    AmpereTheme { SettingsScreen() }
}

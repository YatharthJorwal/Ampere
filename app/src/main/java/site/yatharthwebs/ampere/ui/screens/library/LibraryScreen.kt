package site.yatharthwebs.ampere.ui.screens.library

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import site.yatharthwebs.ampere.R
import site.yatharthwebs.ampere.domain.Track
import site.yatharthwebs.ampere.domain.TrackSearch
import site.yatharthwebs.ampere.domain.TrackSource
import site.yatharthwebs.ampere.ui.components.TrackRow
import site.yatharthwebs.ampere.ui.theme.AmpereTheme
import site.yatharthwebs.ampere.util.AudioPermission
import site.yatharthwebs.ampere.util.findActivity

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
    onTrackClick: (Track) -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var permanentlyDenied by rememberSaveable { mutableStateOf(false) }
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            viewModel.onPermissionChanged(granted)
            val activity = context.findActivity()
            if (!granted && activity != null) {
                permanentlyDenied = !ActivityCompat.shouldShowRequestPermissionRationale(activity, AudioPermission.name)
            }
        }

    if (!state.permissionGranted) {
        PermissionPrompt(
            permanentlyDenied = permanentlyDenied,
            onAllow = { permissionLauncher.launch(AudioPermission.name) },
            onOpenSettings = {
                val intent =
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null),
                    )
                context.startActivity(intent)
            },
            modifier = modifier,
        )
    } else {
        var query by rememberSaveable { mutableStateOf("") }
        val visible = remember(state.tracks, query) { TrackSearch.filter(state.tracks, query) }
        LibraryContent(
            state = state,
            visibleTracks = visible,
            query = query,
            onQueryChange = { query = it },
            onRescan = viewModel::refresh,
            onTrackClick = onTrackClick,
            modifier = modifier,
        )
    }
}

@Composable
private fun PermissionPrompt(
    permanentlyDenied: Boolean,
    onAllow: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.permission_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text =
                stringResource(
                    if (permanentlyDenied) R.string.permission_denied_body else R.string.permission_body,
                ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (permanentlyDenied) {
            Button(onClick = onOpenSettings) { Text(stringResource(R.string.permission_open_settings)) }
        } else {
            Button(onClick = onAllow) { Text(stringResource(R.string.permission_allow)) }
        }
    }
}

@Composable
private fun LibraryContent(
    state: LibraryUiState,
    visibleTracks: List<Track>,
    query: String,
    onQueryChange: (String) -> Unit,
    onRescan: () -> Unit,
    onTrackClick: (Track) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            singleLine = true,
            placeholder = { Text(stringResource(R.string.library_search_hint)) },
        )
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text =
                    context.resources.getQuantityString(
                        R.plurals.library_song_count,
                        visibleTracks.size,
                        visibleTracks.size,
                    ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            TextButton(onClick = onRescan, enabled = !state.loading) {
                Text(stringResource(R.string.library_rescan))
            }
        }
        when {
            state.loading && !state.loaded -> {
                Message(stringResource(R.string.library_loading))
            }

            state.failed -> {
                Message(stringResource(R.string.library_error))
            }

            state.loaded && state.tracks.isEmpty() -> {
                Message(stringResource(R.string.library_empty_title), stringResource(R.string.library_empty_body))
            }

            visibleTracks.isEmpty() -> {
                Message(stringResource(R.string.library_no_results))
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(items = visibleTracks, key = { it.id }) { track ->
                        TrackRow(track = track, onClick = { onTrackClick(track) })
                    }
                }
            }
        }
    }
}

@Composable
private fun Message(
    title: String,
    body: String? = null,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        if (body != null) {
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private val previewTracks =
    listOf(
        Track(
            1L,
            "content://media/1",
            "Blue Monday",
            "New Order",
            "Power, Corruption & Lies",
            449_000L,
            1,
            0L,
            "audio/mpeg",
        ),
        Track(2L, "content://media/2", "True Faith", "New Order", "Substance", 356_000L, 2, 0L, "audio/mpeg"),
        Track(3L, "content://media/3", "Untitled", "", "", 61_000L, 3, 0L, "audio/flac"),
    )

@Preview(showBackground = true)
@Composable
private fun LibraryContentPreview() {
    AmpereTheme {
        LibraryContent(
            state = LibraryUiState(permissionGranted = true, loaded = true, tracks = previewTracks),
            visibleTracks = previewTracks,
            query = "",
            onQueryChange = {},
            onRescan = {},
            onTrackClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PermissionPromptPreview() {
    AmpereTheme { PermissionPrompt(permanentlyDenied = false, onAllow = {}, onOpenSettings = {}) }
}

/** Fake source for previews of screens that need a view model. */
object PreviewTrackSource : TrackSource {
    override suspend fun loadTracks(): List<Track> = previewTracks
}

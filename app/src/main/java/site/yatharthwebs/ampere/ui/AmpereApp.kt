package site.yatharthwebs.ampere.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import site.yatharthwebs.ampere.R
import site.yatharthwebs.ampere.ui.components.MiniPlayerPlaceholder
import site.yatharthwebs.ampere.ui.screens.NowPlayingScreen
import site.yatharthwebs.ampere.ui.screens.PlaylistScreen
import site.yatharthwebs.ampere.ui.screens.SettingsScreen
import site.yatharthwebs.ampere.ui.screens.library.LibraryScreen
import site.yatharthwebs.ampere.ui.screens.library.LibraryViewModel
import site.yatharthwebs.ampere.ui.screens.library.PreviewTrackSource
import site.yatharthwebs.ampere.ui.theme.AmpereTheme
import site.yatharthwebs.ampere.ui.theme.LocalSkin

/** Top-level destinations. Radio joins in Phase 4 (docs/UI_SPEC.md section 2). */
enum class Destination(
    @param:StringRes val labelRes: Int,
) {
    Library(R.string.nav_library),
    Playlist(R.string.nav_playlist),
    NowPlaying(R.string.nav_now_playing),
    Settings(R.string.nav_settings),
}

@Composable
fun AmpereApp(libraryViewModel: LibraryViewModel) {
    val skin = LocalSkin.current
    var current by rememberSaveable { mutableStateOf(Destination.NowPlaying) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Column {
                if (current != Destination.NowPlaying) {
                    MiniPlayerPlaceholder(onClick = { current = Destination.NowPlaying })
                }
                NavigationBar(containerColor = skin.colors.surface) {
                    Destination.entries.forEach { destination ->
                        val selected = destination == current
                        NavigationBarItem(
                            selected = selected,
                            onClick = { current = destination },
                            icon = { NavDot(selected = selected) },
                            label = {
                                Text(
                                    text = stringResource(destination.labelRes),
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Visible,
                                )
                            },
                            colors =
                                NavigationBarItemDefaults.colors(
                                    selectedTextColor = skin.colors.accent,
                                    unselectedTextColor = skin.colors.onSurfaceMuted,
                                    indicatorColor = skin.colors.selectionBg,
                                ),
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            when (current) {
                Destination.Library -> LibraryScreen(viewModel = libraryViewModel)
                Destination.Playlist -> PlaylistScreen()
                Destination.NowPlaying -> NowPlayingScreen()
                Destination.Settings -> SettingsScreen()
            }
        }
    }
}

@Composable
private fun NavDot(selected: Boolean) {
    val colors = LocalSkin.current.colors
    Box(
        modifier =
            Modifier
                .size(10.dp)
                .background(
                    color = if (selected) colors.accent else colors.onSurfaceMuted,
                    shape = CircleShape,
                ),
    )
}

@Preview(showBackground = true)
@Composable
private fun AmpereAppPreview() {
    AmpereTheme { AmpereApp(libraryViewModel = LibraryViewModel(PreviewTrackSource)) }
}

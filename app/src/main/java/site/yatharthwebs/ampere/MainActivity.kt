package site.yatharthwebs.ampere

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import site.yatharthwebs.ampere.ui.AmpereApp
import site.yatharthwebs.ampere.ui.screens.library.LibraryViewModel
import site.yatharthwebs.ampere.ui.theme.AmpereTheme
import site.yatharthwebs.ampere.util.AudioPermission

class MainActivity : ComponentActivity() {
    private val libraryViewModel: LibraryViewModel by viewModels {
        LibraryViewModel.Factory((application as AmpereApplication).container.trackSource)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Default skin is dark, so keep system bar icons light. Becomes skin-driven in P3.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        libraryViewModel.onPermissionChanged(AudioPermission.isGranted(this))
        setContent {
            AmpereTheme {
                AmpereApp(libraryViewModel = libraryViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Picks up a permission change made in system Settings while the app was in the background.
        libraryViewModel.onPermissionChanged(AudioPermission.isGranted(this))
    }
}

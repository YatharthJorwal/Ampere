package site.yatharthwebs.ampere

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import site.yatharthwebs.ampere.ui.AmpereApp
import site.yatharthwebs.ampere.ui.theme.AmpereTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Default skin is dark, so keep system bar icons light. Becomes skin-driven in P3.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        setContent {
            AmpereTheme {
                AmpereApp()
            }
        }
    }
}

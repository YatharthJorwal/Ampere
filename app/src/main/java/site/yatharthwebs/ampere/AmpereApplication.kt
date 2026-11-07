package site.yatharthwebs.ampere

import android.app.Application

class AmpereApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}

package site.yatharthwebs.ampere.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import site.yatharthwebs.ampere.domain.Track
import site.yatharthwebs.ampere.domain.TrackSource

data class LibraryUiState(
    val permissionGranted: Boolean = false,
    val loading: Boolean = false,
    val loaded: Boolean = false,
    val failed: Boolean = false,
    val tracks: List<Track> = emptyList(),
)

class LibraryViewModel(
    private val source: TrackSource,
) : ViewModel() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val mutableState = MutableStateFlow(LibraryUiState())
    val state: StateFlow<LibraryUiState> = mutableState.asStateFlow()

    /** Called when the app resumes and after a permission dialog, so a change in Settings is picked up. */
    fun onPermissionChanged(granted: Boolean) {
        mutableState.update { it.copy(permissionGranted = granted) }
        val current = mutableState.value
        if (granted && !current.loaded && !current.loading) refresh()
    }

    fun refresh() {
        if (mutableState.value.loading) return
        mutableState.update { it.copy(loading = true, failed = false) }
        scope.launch {
            try {
                val tracks = source.loadTracks().sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.title })
                mutableState.update { it.copy(loading = false, loaded = true, tracks = tracks) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                mutableState.update { it.copy(loading = false, failed = true) }
            }
        }
    }

    override fun onCleared() {
        scope.cancel()
    }

    class Factory(
        private val source: TrackSource,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = LibraryViewModel(source) as T
    }
}

package site.yatharthwebs.ampere

import android.content.Context
import site.yatharthwebs.ampere.data.mediastore.MediaStoreTrackSource
import site.yatharthwebs.ampere.domain.TrackSource

/** Hand-wired dependencies (docs/DECISIONS.md ADR-0021). Grows as services and databases arrive. */
class AppContainer(
    context: Context,
) {
    val trackSource: TrackSource = MediaStoreTrackSource(context)
}

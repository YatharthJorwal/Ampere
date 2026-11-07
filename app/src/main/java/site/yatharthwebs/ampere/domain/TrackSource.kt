package site.yatharthwebs.ampere.domain

/** Where tracks come from. Implemented over MediaStore now; tests and previews use fakes. */
interface TrackSource {
    suspend fun loadTracks(): List<Track>
}

package site.yatharthwebs.ampere.domain

/** One audio file in the library. Empty [artist] or [album] means unknown; the UI shows a placeholder. */
data class Track(
    val id: Long,
    val uri: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationMs: Long,
    val trackNumber: Int,
    val dateAddedSec: Long,
    val mimeType: String,
)

package site.yatharthwebs.ampere.domain

/** As-you-type search across title, artist and album (docs/SPEC.md F-LIB-03). */
object TrackSearch {
    private val whitespace = Regex("\\s+")

    /** Keeps tracks where every whitespace-separated term appears in title, artist or album, ignoring case. */
    fun filter(
        tracks: List<Track>,
        query: String,
    ): List<Track> {
        val terms =
            query
                .trim()
                .lowercase()
                .split(whitespace)
                .filter { it.isNotEmpty() }
        if (terms.isEmpty()) return tracks
        return tracks.filter { track ->
            val haystack = "${track.title} ${track.artist} ${track.album}".lowercase()
            terms.all { haystack.contains(it) }
        }
    }
}

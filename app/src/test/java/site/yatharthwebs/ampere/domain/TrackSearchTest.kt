package site.yatharthwebs.ampere.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class TrackSearchTest {
    private fun track(
        id: Long,
        title: String,
        artist: String = "",
        album: String = "",
    ) = Track(
        id = id,
        uri = "content://media/$id",
        title = title,
        artist = artist,
        album = album,
        durationMs = 180_000L,
        trackNumber = 1,
        dateAddedSec = 0L,
        mimeType = "audio/mpeg",
    )

    private val tracks =
        listOf(
            track(1, "Blue Monday", "New Order", "Power, Corruption & Lies"),
            track(2, "True Faith", "New Order", "Substance"),
            track(3, "Temptation", "New Order", "Substance"),
            track(4, "Blue Jeans", "Lana Del Rey", "Born to Die"),
        )

    @Test
    fun blankQueryKeepsEverything() {
        assertEquals(tracks, TrackSearch.filter(tracks, ""))
        assertEquals(tracks, TrackSearch.filter(tracks, "   "))
    }

    @Test
    fun matchesIgnoringCase() {
        assertEquals(listOf(1L, 4L), TrackSearch.filter(tracks, "BLUE").map { it.id })
    }

    @Test
    fun allTermsMustMatchAcrossFields() {
        assertEquals(listOf(1L), TrackSearch.filter(tracks, "blue order").map { it.id })
        assertEquals(listOf(2L, 3L), TrackSearch.filter(tracks, "substance new").map { it.id })
    }

    @Test
    fun noMatchGivesEmptyList() {
        assertEquals(emptyList<Track>(), TrackSearch.filter(tracks, "zzz"))
    }
}

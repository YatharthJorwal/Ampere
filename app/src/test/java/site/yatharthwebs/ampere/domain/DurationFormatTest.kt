package site.yatharthwebs.ampere.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class DurationFormatTest {
    @Test
    fun formatsMinutesAndSeconds() {
        assertEquals("0:00", DurationFormat.format(0L))
        assertEquals("0:09", DurationFormat.format(9_999L))
        assertEquals("3:05", DurationFormat.format(185_000L))
        assertEquals("59:59", DurationFormat.format(3_599_000L))
    }

    @Test
    fun formatsHours() {
        assertEquals("1:00:00", DurationFormat.format(3_600_000L))
        assertEquals("2:03:04", DurationFormat.format(7_384_000L))
    }

    @Test
    fun negativeIsZero() {
        assertEquals("0:00", DurationFormat.format(-5_000L))
    }
}

package site.yatharthwebs.ampere.dsp

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EqBandsTest {
    @Test
    fun hasTenBandsAtClassicCenters() {
        assertEquals(EqBands.BAND_COUNT, EqBands.CENTER_FREQUENCIES_HZ.size)
        assertEquals(
            listOf(60.0, 170.0, 310.0, 600.0, 1000.0, 3000.0, 6000.0, 12000.0, 14000.0, 16000.0),
            EqBands.CENTER_FREQUENCIES_HZ,
        )
    }

    @Test
    fun bandsAscendAndFitBelowNyquistAt44k() {
        val nyquist = 44100.0 / 2
        EqBands.CENTER_FREQUENCIES_HZ.zipWithNext().forEach { (a, b) -> assertTrue(a < b) }
        assertTrue(EqBands.CENTER_FREQUENCIES_HZ.last() < nyquist)
    }

    @Test
    fun labelsAreShort() {
        val labels = (0 until EqBands.BAND_COUNT).map { EqBands.label(it) }
        assertEquals(listOf("60", "170", "310", "600", "1k", "3k", "6k", "12k", "14k", "16k"), labels)
    }

    @Test
    fun clampKeepsGainInRange() {
        assertEquals(12.0, EqBands.clampGainDb(30.0), 0.0)
        assertEquals(-12.0, EqBands.clampGainDb(-30.0), 0.0)
        assertEquals(3.5, EqBands.clampGainDb(3.5), 0.0)
    }
}

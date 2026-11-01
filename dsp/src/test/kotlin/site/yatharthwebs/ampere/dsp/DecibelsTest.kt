package site.yatharthwebs.ampere.dsp

import org.junit.Assert.assertEquals
import org.junit.Test

class DecibelsTest {
    private val tolerance = 1e-9

    @Test
    fun zeroDbIsUnityGain() {
        assertEquals(1.0, Decibels.toLinear(0.0), tolerance)
    }

    @Test
    fun plusAndMinusTwentyDbAreTenAndOneTenth() {
        assertEquals(10.0, Decibels.toLinear(20.0), tolerance)
        assertEquals(0.1, Decibels.toLinear(-20.0), tolerance)
    }

    @Test
    fun sixDbIsRoughlyDoubleAmplitude() {
        assertEquals(2.0, Decibels.toLinear(6.0206), 1e-3)
    }

    @Test
    fun roundTripKeepsValue() {
        var db = -24.0
        while (db <= 24.0) {
            assertEquals(db, Decibels.fromLinear(Decibels.toLinear(db)), 1e-9)
            db += 0.5
        }
    }

    @Test
    fun silenceMapsToFloor() {
        assertEquals(Decibels.FLOOR_DB, Decibels.fromLinear(0.0), tolerance)
        assertEquals(Decibels.FLOOR_DB, Decibels.fromLinear(-1.0), tolerance)
    }
}

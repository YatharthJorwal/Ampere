package site.yatharthwebs.ampere.dsp

import kotlin.math.log10
import kotlin.math.pow

/** Conversions between decibels (amplitude, 20 * log10) and linear gain. */
object Decibels {
    /** Value returned for silence (zero or negative linear input). */
    const val FLOOR_DB: Double = -120.0

    /** Linear amplitude factor for [db] decibels. 0 dB is 1.0, +20 dB is 10.0, -20 dB is 0.1. */
    fun toLinear(db: Double): Double = 10.0.pow(db / 20.0)

    /** Decibels for a linear amplitude factor, never below [FLOOR_DB]. */
    fun fromLinear(linear: Double): Double = if (linear <= 0.0) FLOOR_DB else maxOf(20.0 * log10(linear), FLOOR_DB)
}

package site.yatharthwebs.ampere.dsp

/** Layout of the 10-band equalizer (docs/SPEC.md F-EQ-01). */
object EqBands {
    const val BAND_COUNT: Int = 10
    const val MIN_GAIN_DB: Double = -12.0
    const val MAX_GAIN_DB: Double = 12.0

    /** Band center frequencies in Hz, ascending. */
    val CENTER_FREQUENCIES_HZ: List<Double> =
        listOf(
            60.0,
            170.0,
            310.0,
            600.0,
            1000.0,
            3000.0,
            6000.0,
            12000.0,
            14000.0,
            16000.0,
        )

    /** Short UI label for band [index]: "60", "170", ... "1k", "3k", ... "16k". */
    fun label(index: Int): String {
        val hz = CENTER_FREQUENCIES_HZ[index]
        return if (hz >= 1000.0) "${(hz / 1000.0).toInt()}k" else hz.toInt().toString()
    }

    /** Limits [db] to the supported gain range. */
    fun clampGainDb(db: Double): Double = db.coerceIn(MIN_GAIN_DB, MAX_GAIN_DB)
}

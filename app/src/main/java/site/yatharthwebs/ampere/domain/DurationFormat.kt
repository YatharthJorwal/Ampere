package site.yatharthwebs.ampere.domain

import java.util.Locale

/** Formats milliseconds as m:ss, or h:mm:ss from one hour up. Negative input is treated as zero. */
object DurationFormat {
    fun format(ms: Long): String {
        val totalSeconds = ms.coerceAtLeast(0L) / 1000L
        val hours = totalSeconds / 3600L
        val minutes = (totalSeconds % 3600L) / 60L
        val seconds = totalSeconds % 60L
        return if (hours > 0L) {
            String.format(Locale.ROOT, "%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.ROOT, "%d:%02d", minutes, seconds)
        }
    }
}

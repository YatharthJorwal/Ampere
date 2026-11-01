import com.diffplug.spotless.LineEnding

// Top-level build file. Module-level configuration lives in app/ and dsp/.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.spotless)
}

spotless {
    lineEndings = LineEnding.UNIX
    kotlin {
        target("app/src/**/*.kt", "dsp/src/**/*.kt")
        ktlint(libs.versions.ktlint.get())
    }
    kotlinGradle {
        target("*.gradle.kts", "app/*.gradle.kts", "dsp/*.gradle.kts")
        ktlint(libs.versions.ktlint.get())
    }
}

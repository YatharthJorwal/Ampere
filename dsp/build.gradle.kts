import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// Pure Kotlin/JVM module: no android.* imports allowed (see docs/ARCHITECTURE.md).
plugins {
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    testImplementation(libs.junit)
}

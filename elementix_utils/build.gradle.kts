import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

plugins {
    kotlin("multiplatform")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    js {
        binaries.executable()
        browser {
            webpackTask(Action {
                output.libraryTarget = "commonjs2"
                devtool = "inline-source-map"
            })
        }
    }
    jvm()
    sourceSets {
        val commonMain by getting
    }
}

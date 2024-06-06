import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
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
    jvm {
        withJava()
        mainRun {
            mainClass = "elementix.test.MainKt"
        }
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.0")
                implementation(project(":elementix_dom"))
                implementation(npm("source-map-loader", ">=0.2.4"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.0")
            }
        }

    }
}

dependencies {
    commonMainApi(project(":elementix_trpc"))
}

tasks.withType(KotlinWebpack::class.java).forEach { t ->
    t.inputs.files(fileTree("src/jsMain/resources"))
}
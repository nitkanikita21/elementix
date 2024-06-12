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
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        /*val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.9.3-pre.346")
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.8.1")
            }
        }*/
    }
}

dependencies {
    commonMainApi(project(":elementix_utils"))
}

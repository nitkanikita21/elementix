import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    js {
        browser()
    }
    sourceSets {
        val commonMain by getting
        val jsMain by getting
        val jvmMain by getting {
            dependencies {
                implementation("com.squareup:kotlinpoet:1.17.0")
                implementation("com.google.devtools.ksp:symbol-processing-api:1.9.24-1.0.20")
            }
        }
    }
}

dependencies {

}

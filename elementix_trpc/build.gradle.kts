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
    jvm()
    sourceSets {
        val jsMain by getting {
            dependencies {
                api("io.ktor:ktor-client-js:3.0.0-beta-1")
                api("io.ktor:ktor-client-logging:3.0.0-beta-1")
                api("io.ktor:ktor-client-content-negotiation:3.0.0-beta-1")
                implementation(npm("html-webpack-plugin", ">=5.6.0"))
                implementation(npm("source-map-loader", ">=0.2.4"))
                implementation(project(":elementix_reactivity"))
            }

        }
        val jvmMain by getting {
            dependencies {
                api("io.ktor:ktor-server-netty:3.0.0-beta-1")
                api("io.ktor:ktor-server-core:3.0.0-beta-1")
                api("io.ktor:ktor-server-content-negotiation:3.0.0-beta-1")
                api("ch.qos.logback:logback-classic:1.5.6")
                api("io.ktor:ktor-server-cors:3.0.0-beta-1")
                runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.8.1")

            }
        }
    }
}

dependencies {
    commonMainApi("io.konform:konform:0.5.0")
    commonMainApi("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    commonMainApi("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.3")
    commonMainApi("io.ktor:ktor-serialization-kotlinx-json:3.0.0-beta-1")
    commonMainApi("io.ktor:ktor-client-core:3.0.0-beta-1")
    commonMainApi(project(":elementix_utils"))
}

tasks.withType(KotlinWebpack::class.java).forEach { t ->
    t.inputs.files(fileTree("src/jsMain/resources"))
}

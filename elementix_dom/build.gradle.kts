plugins {
    kotlin("multiplatform") version "1.9.0"
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
            /*commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }*/
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.9.1")
                implementation(project(":elementix_reactivity"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.9.1")
                implementation(project(":elementix_reactivity"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
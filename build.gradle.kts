import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

plugins {
    kotlin("multiplatform") version "1.9.24" apply false
    kotlin("plugin.serialization") version "1.9.24" apply false
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

subprojects {
    tasks.withType<KotlinJsCompile>().configureEach {
        println(this.name)
        kotlinOptions {
            sourceMap = true
            sourceMapEmbedSources = "always"
        }
    }
}
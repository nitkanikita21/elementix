plugins {
    kotlin("multiplatform") version "1.9.0" apply false
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
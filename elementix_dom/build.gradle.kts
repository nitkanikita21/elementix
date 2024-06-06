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
    applyDefaultHierarchyTemplate()

    js {
        binaries.executable()
        browser {
            webpackTask(Action {
                output.libraryTarget = "commonjs2"
                devtool = "inline-source-map"
            })
        }
    }
    sourceSets {



        val jsMain by getting {
        }
    }
}

dependencies {
    commonMainApi(project(":elementix_reactivity"))
    commonMainApi(project(":elementix_dom_ksp_processor"))
    add("kspCommonMainMetadata", project(":elementix_dom_ksp_processor"))
    add("kspJs", project(":elementix_dom_ksp_processor"))
}


tasks.withType(KotlinWebpack::class.java).forEach { t ->
    t.inputs.files(fileTree("src/jsMain/resources"))
}

ksp {}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "Project"
include(":elementix_dom")
include(":elementix_reactivity")
include(":elementix_dom_generated")
include(":elementix_trpc")
include(":elementix_test")
include(":elementix_dom_ksp_processor")

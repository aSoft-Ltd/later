pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
}

rootProject.name = "later"

include(":later-core")
include(":later-ktx")
include(":later-test-expect")
project(":later-test-expect").projectDir = File("later-test/later-test-expect")

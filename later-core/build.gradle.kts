plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
    id("io.codearte.nexus-staging")
    signing
}

kotlin {
    multiplatformLib()
    sourceSets {
        val commonTest by getting {
            dependencies {
                api(asoft("test-coroutines", vers.asoft.test))
            }
        }

        val jvmMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${vers.kotlinx.coroutines}")
            }
        }

        val jvmTest by getting {
            dependsOn(commonTest)
            dependsOn(jvmMain)
        }

        val jsMain by getting {}

        val jsTest by getting {
            dependsOn(commonTest)
            dependsOn(jsMain)
        }
    }
}

aSoftOSSLibrary(
    version = vers.asoft.later,
    description = "An multiplatform representation of a Promised based api"
)
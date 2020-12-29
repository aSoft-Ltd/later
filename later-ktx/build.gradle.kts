plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
    id("io.codearte.nexus-staging")
    signing
}

kotlin {
    jvm {
        targetJava("1.8")
        withJava()
    }
    js(IR) {
        browser()
        nodejs {
            testTask {
                useMocha {
                    timeout = "10s"
                }
            }
        }
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":later-core"))
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${vers.kotlinx.coroutines}")
            }
        }

        val commonTest by getting {
            dependencies {
                api(asoft("test-coroutines", vers.asoft.test))
                api(asoft("expect-core",vers.asoft.expect))
            }
        }
    }
}

aSoftOSSLibrary(
    version = vers.asoft.later,
    description = "Extensions of the promise based api to be easily used in kotlin"
)
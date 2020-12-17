plugins {
    kotlin("multiplatform") version vers.kotlin
    kotlin("plugin.serialization") version vers.kotlin
    id("tz.co.asoft.library") version vers.asoft.builders
    id("io.codearte.nexus-staging") version vers.nexus_staging
    signing
}

kotlin {
    jvm {
        targetJava("1.8")
        withJava()
    }
    js(IR) {
        browser()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${vers.kotlinx.coroutines}")
            }
        }

        val commonTest by getting {
            dependencies {
                api(asoft("test", vers.asoft.test))
            }
        }
    }
}

aSoftLibrary(
    version = vers.asoft.later,
    description = "An Either<L,R> multiplatform serializable datatype"
)
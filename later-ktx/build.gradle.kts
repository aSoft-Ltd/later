plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
    id("io.codearte.nexus-staging")
    signing
}

kotlin {
    jvm {
        library()
        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }
    js(IR) { library() }

    val darwinTargets = listOf(
        macosX64(),
        iosArm32(),
        iosX64(),
        iosArm64(),
        watchosArm64(),
        watchosArm32(),
        watchosX86(),
        tvosArm64(),
        tvosX64()
    )

    val linuxTargets = listOf(
        linuxX64()
    )
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
                api(asoft("expect-core", vers.asoft.expect))
            }
        }

        val jsMain by getting {}

        (darwinTargets + linuxTargets).forEach {
            val main by it.compilations.getting {}
            main.defaultSourceSet {
                dependsOn(jsMain)
            }
        }
    }
}

aSoftOSSLibrary(
    version = vers.asoft.later,
    description = "Extensions of the promise based api to be easily used in kotlin"
)
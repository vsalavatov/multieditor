plugins {
    kotlin("multiplatform") version "1.6.10"
    id("org.jetbrains.compose") version "1.0.1"
}

group = "dev.salavatov"
version = "1.0"

repositories {
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven {
        url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") // for ktor 2.0.0-eap
    }
    mavenCentral()

    mavenLocal() // for local testing
}

val serializationVersion: String by project

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

                implementation("dev.salavatov:multifs:0.0.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.ui)
                api(compose.material)
                api(compose.desktop.currentOs)

                implementation("dev.salavatov:multifs-jvm:0.0.1")

                implementation("ch.qos.logback:logback-classic:1.2.6") // TODO: remove; just for testing purposes
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            fun kotlinw(target: String): String =
                "org.jetbrains.kotlin-wrappers:kotlin-$target"

            val kotlinWrappersVersion = "0.0.1-pre.284-kotlin-1.6.10"

            dependencies {
                implementation(project.dependencies.enforcedPlatform(kotlinw("wrappers-bom:${kotlinWrappersVersion}")))
                implementation(kotlinw("react"))
                implementation(kotlinw("react-dom"))
                // implementation(kotlinw("react-table"))
                // implementation(kotlinw("styled"))
                // other wrappers
            }
        }
        val jsTest by getting
    }
}

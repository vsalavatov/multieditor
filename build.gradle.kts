plugins {
    kotlin("multiplatform") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.0-beta5"
}

group = "dev.salavatov"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven {
        url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") // for ktor 2.0.0-eap
    }

    mavenLocal() // for local testing
}

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
                // implementation("dev.salavatov:multifs:0.0.1")
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
                // api(compose.ui)
                api(compose.material)
                api(compose.desktop.currentOs)

                implementation("dev.salavatov:multifs-jvm:0.0.1")
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.206-kotlin-1.5.10")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:17.0.2-pre.206-kotlin-1.5.10")
            }
        }
        val jsTest by getting
    }
}

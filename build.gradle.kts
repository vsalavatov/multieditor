import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    kotlin("multiplatform") version Versions.kotlin
    id("org.jetbrains.compose") version Versions.compose
    id("com.android.application") version Versions.androidGradlePlugin
}

group = "dev.salavatov"
version = "1.0"

repositories {
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    mavenCentral()

    mavenLocal() // for local testing
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
            kotlinOptions.freeCompilerArgs += listOf(
                "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlin.RequiresOptIn",
            )
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }
    android {
        publishAllLibraryVariants()
    }
    js(IR) {
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
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinSerialization}")

                api(compose.runtime)

                implementation("dev.salavatov:multifs:${Versions.multifs}")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val commonJvmAndroid = create("commonJvmAndroid") {
            dependsOn(commonMain)
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.ui)
                api(compose.material)
            }
        }
        val jvmMain by getting {
            dependsOn(commonJvmAndroid)
            dependencies {
                api(compose.foundation)
                api(compose.ui)
                api(compose.material)

                api(compose.desktop.currentOs)

                implementation("dev.salavatov:multifs-jvm:${Versions.multifs}")
            }
        }
        val jvmTest by getting
        val androidMain by getting {
            dependsOn(commonJvmAndroid)
            dependencies {
                api(compose.foundation)
                api(compose.ui)
                api(compose.material)

                api("androidx.appcompat:appcompat:1.4.1")
                api("androidx.core:core-ktx:1.7.0")
                implementation("androidx.activity:activity-ktx:1.4.0")
                implementation("androidx.activity:activity-compose:1.4.0")
                implementation("com.google.android.gms:play-services-auth:20.1.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.kotlinCoroutines}")

//                implementation("dev.salavatov:multifs-android:${Versions.multifs}")
//                implementation("io.ktor:ktor-client-okhttp:2.0.0")
            }
        }
        val androidTest by getting
        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)

                implementation("dev.salavatov:multifs-js:${Versions.multifs}")
            }
        }
        val jsTest by getting
    }
}
kotlin.sourceSets.all {
    languageSettings.optIn("androidx.compose.ui.text.ExperimentalTextApi")
    languageSettings.optIn("androidx.compose.material.ExperimentalMaterialApi")
}

android {
    sourceSets {
        named("main") {
            val androidMain = "src/androidMain"
            manifest.srcFile("$androidMain/AndroidManifest.xml")
            res.setSrcDirs(listOf("$androidMain/res", "src/commonMain/resources/"))
            java.setSrcDirs(listOf("$androidMain/java"))
            kotlin.setSrcDirs(listOf("$androidMain/kotlin", "src/commonJvmAndroid/kotlin"))
        }
    }

    compileSdk = Versions.androidCompileSdk
    defaultConfig {
        minSdk = Versions.androidMinSdk
        targetSdk = Versions.androidTargetSdk
    }
}

// some strange non-existent module called "androidAndroidTestRelease" appears after gradle configuration
// here is some fix from the internet (https://discuss.kotlinlang.org/t/disabling-androidandroidtestrelease-source-set-in-gradle-kotlin-dsl-script/21448)
project.extensions.findByType<KotlinMultiplatformExtension>()?.let { ext ->
    ext.sourceSets.removeAll { sourceSet ->
        setOf(
            "androidAndroidTestRelease",
            "androidTestFixtures",
            "androidTestFixturesDebug",
            "androidTestFixturesRelease",
        ).contains(sourceSet.name)
    }
}
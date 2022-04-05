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
    maven {
        url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") // for ktor 2.0.0-eap
    }
    mavenCentral()

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
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinSerialization}")

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
            }
        }
        val jvmMain by getting {
            dependsOn(commonJvmAndroid)
//            kotlin.srcDir("src/commonJvmAndroid/kotlin")
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.ui)
                api(compose.material)
                api(compose.desktop.currentOs)

                implementation("dev.salavatov:multifs-jvm:${Versions.multifs}")

                implementation("ch.qos.logback:logback-classic:1.2.6") // TODO: remove; just for testing purposes
            }
        }
        val jvmTest by getting
        val androidMain by getting {
            dependsOn(commonJvmAndroid)
//            kotlin.srcDir("src/commonJvmAndroid/kotlin")
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.ui)
                api(compose.material)
                api("androidx.appcompat:appcompat:1.4.0")
                api("androidx.core:core-ktx:1.7.0")
                implementation("androidx.activity:activity-ktx:1.4.0")
                implementation("androidx.activity:activity-compose:1.4.0")
//                implementation("android.arch.navigation:navigation-fragment-ktx:1.0.0")
//                implementation("android.arch.navigation:navigation-ui-ktx:1.0.0")
            }
        }
        val androidTest by getting
        val jsMain by getting {
            dependencies {
//                implementation("org.jetbrains.kotlin:kotlin-stdlib-js")

                implementation(compose.web.core)
                implementation(compose.runtime)

                implementation("dev.salavatov:multifs-js:${Versions.multifs}")
            }
            /*fun kotlinw(target: String): String =
                "org.jetbrains.kotlin-wrappers:kotlin-$target"

            val kotlinWrappersVersion = "0.0.1-pre.284-kotlin-1.6.10"

            dependencies {
                implementation(project.dependencies.enforcedPlatform(kotlinw("wrappers-bom:${kotlinWrappersVersion}")))
                implementation(kotlinw("react"))
                implementation(kotlinw("react-dom"))
                // implementation(kotlinw("react-table"))
                // implementation(kotlinw("styled"))
                // other wrappers
            }*/
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

    compileSdkVersion(Versions.androidTargetSdk)
    defaultConfig {
        minSdkVersion(Versions.androidMinSdk)
        targetSdkVersion(Versions.androidTargetSdk)
    }
}

// some strange non-existent module called "androidAndroidTestRelease" appears after gradle configuration
// here is some fix from the internet (https://discuss.kotlinlang.org/t/disabling-androidandroidtestrelease-source-set-in-gradle-kotlin-dsl-script/21448)
subprojects {
    afterEvaluate {
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
    }
}

//dependencies {
//    implementation(kotlin("stdlib-jdk8"))
//}
//val compileKotlin: KotlinCompile by tasks
//compileKotlin.kotlinOptions {
//    jvmTarget = "1.8"
//}
//val compileTestKotlin: KotlinCompile by tasks
//compileTestKotlin.kotlinOptions {
//    jvmTarget = "1.8"
//}
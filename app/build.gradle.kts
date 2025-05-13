@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose)
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
        }
    }
    jvm("desktop")
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "blueprintDemoCompose"
            isStatic = true
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        all {
            languageSettings {
                optIn("androidx.compose.ui.ExperimentalComposeUiApi")
                optIn("com.popovanton0.blueprint.ExperimentalBlueprintApi")
            }
        }

        commonMain.dependencies {
            implementation(project(":blueprint"))
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(compose.uiTooling)
            implementation(compose.uiUtil)

            implementation(libs.androidx.coreKtx)
            implementation(libs.androidx.lifecycleRuntimeKtx)
            implementation(libs.androidx.activityCompose)
            // note, these should have been debugImplementations,
            // but that does not seem to work
            implementation(libs.androidx.compose.ui.tooling)
            implementation(libs.androidx.compose.ui.testManifest)
        }

        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(compose.preview)
            implementation(compose.uiTooling)
            implementation(compose.uiUtil)
        }
    }

}

android {
    namespace = "com.popovanton0.blueprint.app"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.popovanton0.blueprint.app"
        minSdk = 21
        targetSdk = libs.versions.compileSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles("proguard-android-optimize.txt", "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packagingOptions.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
}

compose.desktop {
    application {
        mainClass = "com.popovanton0.blueprint.app.MainKt"
    }
}

@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget


plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.binaryCompatibility)
    id("maven-publish")
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
        }
    }
    jvm("desktop")
    js {
        browser()
        useEsModules()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "blueprint"
            isStatic = true
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        all {
            languageSettings {
                optIn("com.popovanton0.blueprint.ExperimentalBlueprintApi")
            }
        }
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(libs.androidx.annotation)
            }
        }
        commonTest {

        }
        androidUnitTest {
            dependencies {
                implementation(libs.junit)
                implementation(compose.material3)
                implementation(libs.testParameterInjector)
                implementation(libs.equalsverifier)
                implementation(libs.androidx.testJunit)
                implementation(libs.androidx.compose.ui.testJunit)
                implementation(libs.androidx.compose.ui.tooling)
            }
        }
        androidInstrumentedTest {

        }
    }
}

android {
    namespace = "com.popovanton0.blueprint"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles("proguard-android-optimize.txt", "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        explicitApi()
    }
}

tasks.named("assemble") {
    dependsOn("check")
}
//
//dependencies {
//    implementation(libs.androidx.compose.ui.util)
//    implementation(libs.androidx.compose.ui)
//    implementation(libs.androidx.compose.foundation)
//    implementation(libs.androidx.annotation)
//
//    debugImplementation(libs.androidx.compose.ui.tooling)
//    debugImplementation(libs.androidx.compose.ui.testManifest)
//
//    testImplementation(libs.junit)
//    implementation(libs.androidx.compose.ui.tooling)
//    testImplementation(libs.androidx.compose.material)
//    testImplementation(libs.testParameterInjector)
//    testImplementation(libs.equalsverifier)
//
//    androidTestImplementation(libs.androidx.testJunit)
//    androidTestImplementation(libs.androidx.compose.ui.testJunit)
//}

group = "com.popovanton0.blueprint"
version = libs.versions.blueprint.get()

publishing {
}
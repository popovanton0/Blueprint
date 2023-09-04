@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.binaryCompatibility)
    id("maven-publish")
}

android {
    namespace = "com.popovanton0.blueprint"
    compileSdk = 33

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
        jvmToolchain(17)
        explicitApi()
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-opt-in=com.popovanton0.blueprint.ExperimentalBlueprintApi")
    }

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
}

tasks.named("assemble") {
    dependsOn("check")
}

dependencies {
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.annotation)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)

    testImplementation(libs.junit)
    implementation(libs.androidx.compose.ui.tooling)
    testImplementation(libs.androidx.compose.material)
    testImplementation(libs.testParameterInjector)
    testImplementation(libs.equalsverifier)

    androidTestImplementation(libs.androidx.testJunit)
    androidTestImplementation(libs.androidx.compose.ui.testJunit)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.popovanton0.blueprint"
            artifactId = "blueprint"
            version = "1.0.0-alpha02"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
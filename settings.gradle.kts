@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    // note, JS targets seem to be incompatible with FAIL_ON_PROJECT_REPOS
    // see https://youtrack.jetbrains.com/issue/KT-51379/Build-fails-when-using-RepositoriesMode.FAILONPROJECTREPOS-with-kotlin-multiplatform-projects
    // (there's a workaround that uses local node/npm, but that'd probably be more confusing)
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
    }
}
rootProject.name = "Blueprint"
include(":app")
include(":blueprint")
include(":blueprint-no-op")

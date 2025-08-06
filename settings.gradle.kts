pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://github.com/square/okhttp/releases")
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://dl.google.com/dl/android/maven2/")

    }
}

rootProject.name = "ChitChat"
include(":app")

rootProject.name = "kotlin_markdown"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("dependencies/libs.versions.toml"))
        }
        create("testLibs") {
            from(files("dependencies/testLibs.versions.toml"))
        }
    }

    //Used for plugins resolution
    repositories {
        gradlePluginPortal()
        google()
        mavenLocal()
        mavenCentral()
    }
}

//Used for project dependencies resolution
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

plugins {
    id("com.gradle.enterprise") version "3.10.3"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlways()
    }
}

include("basic")


pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven("https://maven.neoforged.net/releases") {
            name = "NeoForged"
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include("ContainerSyncing")
include("Conditional")
include("MathExpressions")
include("Jsoning")
include("Functions")
include("Registering")
include("DataRegistering")

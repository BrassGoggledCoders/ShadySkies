plugins {
    id("java-library")
    id("eclipse")
    id("idea")
    id("maven-publish")
    id("net.neoforged.gradle.userdev") version "7.0.97"
}

group = "xyz.brassgoggledcoders.shadyskies"
version = "3.0.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(
        group = "net.neoforged",
        name = "neoforge",
        version = "20.4.147-beta"
    )
}

minecraft {

    runs {

    }
}

tasks.jar {
    from(sourceSets.main.get().output)
    manifest {
        attributes(mapOf(
            "FMLModType" to "GAMELIBRARY"
        ))
    }
}

artifacts {
    archives(tasks.jar.get())
}

publishing {
    publications {
        register<MavenPublication>("jar") {
            artifact(tasks.jar.get())
        }
    }
    repositories {
        val deployDir = project.findProperty("DEPLOY_DIR")
        if (deployDir != null) {
            maven(deployDir)
        } else {
            mavenLocal()
        }
    }
}
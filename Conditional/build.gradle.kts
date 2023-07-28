plugins {
    id("java")
    id("maven-publish")
    id("net.neoforged.gradle") version ("[6.0.18,6.2)")
}

group = "xyz.brassgoggledcoders.shadyskies"
version = "2.0.0"

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
    "minecraft"(
        group = "net.neoforged",
        name = "forge",
        version = "1.20.1-47.1.56"
    )
}

minecraft {
    mappings("official", "1.20.1")

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

    finalizedBy("reobfJar")
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
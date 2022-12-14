plugins {
    id("java")
    id("maven-publish")
    id("net.minecraftforge.gradle") version ("5.1.+")
    id("org.parchmentmc.librarian.forgegradle") version ("1.+")
}

group = "xyz.brassgoggledcoders.shadyskies"
version = "1.0.2"

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
        group = "net.minecraftforge",
        name = "forge",
        version = "1.18.2-40.1.84"
    )
}

minecraft {
    mappings("parchment", "2022.03.13-1.18.2")

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
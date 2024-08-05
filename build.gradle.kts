plugins {
    id("java")
    id("maven-publish")
    id("java-library")
    id("eclipse")
    id("idea")
    id("net.neoforged.gradle.userdev") version "7.0.97"
}

childProjects.filter {
    it.key in listOf("Registering","DataRegistering", "VehicularContents")
}.forEach {
    it.value.apply(plugin = "net.neoforged.gradle.userdev")
    it.value.apply(plugin = "maven-publish")

    it.value.group = "xyz.brassgoggledcoders.shadyskies"

    it.value.java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
        withSourcesJar()
    }

    it.value.repositories {
        mavenCentral()
    }

    it.value.dependencies {
        implementation(
            group = "net.neoforged",
            name = "neoforge",
            version = "20.4.147-beta"
        )
    }

    it.value.minecraft {
        runs {

        }
    }

    it.value.tasks.jar {
        from(it.value.sourceSets.main.get().output)
        manifest {
            attributes(mapOf(
                "FMLModType" to "GAMELIBRARY"
            ))
        }
    }

    it.value.artifacts {
        archives(it.value.tasks.jar.get())
    }

    it.value.publishing {
        publications {
            register<MavenPublication>("jar") {
                artifact(it.value.tasks.jar.get())
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
}
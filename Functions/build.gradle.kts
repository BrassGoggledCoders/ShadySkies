plugins {
    id("java")
    id("maven-publish")
}

group = "xyz.brassgoggledcoders.shadyskies"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {

}

tasks.jar {
    from(sourceSets.main.get().output)
    manifest {
        attributes(mapOf(
            "FMLModType" to "GAMELIBRARY"
        ))
    }
}

tasks.test {
    useJUnitPlatform()
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
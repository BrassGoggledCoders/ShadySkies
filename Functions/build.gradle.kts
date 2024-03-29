plugins {
    id("java")
}

group = "xyz.brassgoggledcoders.shadyskies"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {

}

tasks.test {
    useJUnitPlatform()
}
plugins {
    kotlin("jvm") version "2.0.0"
}

group = "nz.laspruca"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("reflect"))
    implementation("org.ow2.asm:asm:9.7")
}

tasks.test {
    useJUnitPlatform()
}
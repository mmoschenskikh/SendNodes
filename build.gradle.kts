plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.serialization") version "1.8.21"
    application
}

group = "ru.maxultra"
version = "1.0"

repositories {
    mavenCentral()
}

val ktorVersion: String by project
val kotlinxSerializationVersion: String by project

dependencies {
    implementation("io.ktor:ktor-network:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
}

application {
    mainClass.set("MainKt")
}

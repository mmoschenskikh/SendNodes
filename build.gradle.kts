plugins {
    kotlin("jvm") version "1.8.21"
    application
}

group = "ru.maxultra"
version = "1.0"

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("MainKt")
}

plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.serialization") version "1.8.21"
    application
}

group = "ru.maxultra"
version = "1.2"

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

val fatJar = task("fatJar", type = Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
}

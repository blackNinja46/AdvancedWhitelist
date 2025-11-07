import kotlin.io.path.Path

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "live.blackninja"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://libraries.minecraft.net/")
    }
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("net.dv8tion:JDA:6.1.1")
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
    compileOnly("com.mojang:authlib:1.5.25")
    compileOnly("com.mojang:brigadier:1.0.18")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

java {
    withSourcesJar()
}
plugins {
    id("java-library")
}

group = "dev.httpmarco.polocloud"
version = "3.0.0-pre.8-SNAPSHOT"

repositories {
    mavenCentral()

    maven {
        name = "polocloud-snapshots"
        url = uri("https://central.sonatype.com/repository/maven-snapshots/")
    }
}

dependencies {
    api("io.grpc:grpc-services:1.77.0")
    api("io.grpc:grpc-netty-shaded:1.77.0")

    api("dev.httpmarco.polocloud:proto:3.0.0-pre.8-SNAPSHOT")
    api("dev.httpmarco.polocloud:shared:3.0.0-pre.8-SNAPSHOT")
    implementation("org.jetbrains:annotations:26.0.2-1")
}
plugins {
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    id("com.gradleup.shadow") version "9.0.0"
    id("java-library")
    `maven-publish`
}

group = "dev.httpmarco.polocloud"
version = "3.0.0-pre.7-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()

    maven {
        name = "polocloud-snapshots"
        url = uri("https://central.sonatype.com/repository/maven-snapshots/")
    }
}

tasks.shadowJar {
    archiveClassifier.set(null)

    mergeServiceFiles()
}

dependencies {
    api("io.grpc:grpc-services:1.78.0")
    api("io.grpc:grpc-netty-shaded:1.78.0")

    api("dev.httpmarco.polocloud:proto:3.0.0-pre.7-SNAPSHOT")
    api("dev.httpmarco.polocloud:shared:3.0.0-pre.7-SNAPSHOT")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.3.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.3.0")

    compileOnly("com.google.code.gson:gson:2.13.2")
    compileOnly("org.jetbrains:annotations:26.0.2-1")

}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks.named("shadowJar")) {
                classifier = null
            }

            pom {
                description.set("PoloCloud gRPC API with bundled dependencies")
                url.set("https://github.com/thePolocloud/polocloud")

                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
                developers {
                    developer {
                        name.set("Mirco Lindenau")
                        email.set("mirco.lindenau@gmx.de")
                    }
                }
                scm {
                    url.set("https://github.com/thePolocloud/polocloud")
                    connection.set("scm:git:https://github.com/thePolocloud/polocloud.git")
                    developerConnection.set("scm:git:https://github.com/thePolocloud/polocloud.git")
                }
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/releases/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))

            username.set(System.getenv("ossrhUsername") ?: "")
            password.set(System.getenv("ossrhPassword") ?: "")
        }
    }
    useStaging.set(!version.toString().endsWith("-SNAPSHOT"))
}

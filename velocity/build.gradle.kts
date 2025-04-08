plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "network.tecnocraft"
version = "1.1.0"
java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.velocitypowered.com/snapshots/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    
    implementation("me.carleslc.Simple-YAML:Simple-Yaml:1.8.4")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.11.0")

}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        archiveBaseName.set("paperlink-proxy")
        archiveVersion.set(project.version.toString())
    }
    
    shadowJar {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        archiveBaseName.set("paperlink-proxy")
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("")
    }
    build {
        dependsOn(shadowJar)
    }
}
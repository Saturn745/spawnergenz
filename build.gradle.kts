plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.xhyrom.spawnergenz"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.purpurmc.org/snapshots")
    maven("https://jitpack.io")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://hub.jeff-media.com/nexus/repository/jeff-media-public/")
}

dependencies {
    compileOnly("org.purpurmc.purpur", "purpur-api", "1.19.2-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.3")

    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")

    implementation("dev.jorel:commandapi-shade:8.8.0")
    implementation("com.jeff_media:MorePersistentDataTypes:2.4.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks {
    shadowJar {
        relocate("dev.jorel.commandapi", "me.xhyrom.spawnergenz.libs.commandapi")
        relocate("com.jeff_media.morepersistentdatatypes", "me.xhyrom.spawnergenz.libs.morepersistentdatatypes")
    }
}
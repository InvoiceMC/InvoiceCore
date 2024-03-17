plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"

    kotlin("jvm") version "2.0.0-Beta4"
    `maven-publish`
}

group = "org.github.invoicemc"
version = "0.0.1"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    maven { url = uri("https://oss.sonatype.org/content/groups/public/") }
    maven { url = uri("https://repo.codemc.io/repository/maven-public/") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

    api("org.reflections:reflections:0.9.8")

    // Async Stuff
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.14.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.14.0")

    // Storage
    implementation("dev.dejvokep:boosted-yaml:1.3")
    implementation("com.google.code.gson:gson:2.10.1")

    // Fastboard
    implementation("fr.mrmicky:fastboard:2.1.0")

    implementation("net.kyori:adventure-text-minimessage:4.14.0")
    implementation("com.github.InvoiceMC:Munch:e2ee7d2d4e")
    implementation(kotlin("stdlib-jdk8"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

tasks {
    java {
        withSourcesJar()
        withJavadocJar()
    }

    wrapper {
        gradleVersion = "8.6"
        distributionType = Wrapper.DistributionType.ALL
    }

    shadowJar {
        archiveFileName.set("${project.name}.jar")

        // Relocations
        val relocationPath = "me.outspending.core.invoice.relocations"

        relocate("org.github.shynixn.mccoroutine", "$relocationPath.mccoroutine")
        relocate("dev.dejvokep.boostedyaml", "$relocationPath.boostedyaml")
        relocate("fr.mrmicky.fastboard", "$relocationPath.fastboard")

        destinationDirectory.set(file("E:\\Servers\\Testing\\plugins"))
    }
}

kotlin { jvmToolchain(17) }
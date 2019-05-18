buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath("com.github.jengelman.gradle.plugins:shadow:4.0.2")
    }
}

plugins {
    kotlin("jvm") version "1.3.31"
}

apply {
    plugin("com.github.johnrengelman.shadow")
}

val keycloakVersion = "6.0.1"

repositories {
    jcenter() 
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.keycloak:keycloak-core:$keycloakVersion")
    implementation("org.keycloak:keycloak-server-spi:$keycloakVersion")
    implementation("org.keycloak:keycloak-server-spi-private:$keycloakVersion")
    implementation("khttp:khttp:1.0.0")
}

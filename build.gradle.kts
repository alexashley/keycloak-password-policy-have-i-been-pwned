import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val sourceSets = the<SourceSetContainer>()
val keycloakVersion = "6.0.1"
val junitVersion = "5.4.2"

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

repositories {
    jcenter() 
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.keycloak:keycloak-core:$keycloakVersion")
    implementation("org.keycloak:keycloak-server-spi:$keycloakVersion")
    implementation("org.keycloak:keycloak-server-spi-private:$keycloakVersion")
    implementation("khttp:khttp:1.0.0")

//    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

sourceSets {
    create("acceptance") {
        withConvention(KotlinSourceSet::class) {
            kotlin.srcDir(file("src/acceptance/kotlin"))
            resources.srcDir(file("src/acceptance/resources"))
            compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
            runtimeClasspath += output + compileClasspath
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

task<Test>("acceptance") {
    group = "Verification"
    description = "Runs acceptance tests"
    testClassesDirs = sourceSets["acceptance"].output.classesDirs
    classpath = sourceSets["acceptance"].runtimeClasspath
}

tasks.named<Test>("acceptance") {
    useJUnitPlatform()
}
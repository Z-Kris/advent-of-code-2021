import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    application
}

group = "me.kris"
version = "1.0-SNAPSHOT"

repositories(RepositoryHandler::mavenCentral)

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.5.10")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

application {
    mainClass.set("aoc.AdventOfCode")
}

import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.*

plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("kapt") version "1.4.10"
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup:kotlinpoet:1.7.2")
    implementation("com.google.auto.service:auto-service:1.0-rc7")
    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.2.0")
    kapt("com.google.auto.service:auto-service:1.0-rc7")
}
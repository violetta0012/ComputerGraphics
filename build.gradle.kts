import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    application
    id("org.openjfx.javafxplugin") version "0.0.10"
}

group = "me.violettagerasimova"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}



tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "MainKt"
}

javafx {
    version = "12"
    modules("javafx.controls", "javafx.fxml")
    configuration = "compileOnly"
}
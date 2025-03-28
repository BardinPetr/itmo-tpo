plugins {
    kotlin("jvm") version "2.1.0"
    jacoco
}

group = "ru.bardinpetr.itmo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.kotlin.link")
}

dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("io.kotest:kotest-property:5.9.1")
    testImplementation("io.kotest:kotest-framework-datatest:5.9.1")
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:4.10.0")
    implementation("org.jetbrains.lets-plot:lets-plot-common:4.6.1")
    implementation("org.jetbrains.lets-plot:lets-plot-jfx:4.6.1")
    implementation("org.jetbrains.lets-plot:lets-plot-image-export:4.6.1")
    implementation("space.kscience:plotlykt-core:0.7.1")
    implementation("space.kscience:plotlykt-server:0.7.1")
    testImplementation("io.mockk:mockk:1.13.16")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
    finalizedBy(tasks.jacocoTestReport)
}

kotlin {
    jvmToolchain(21)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

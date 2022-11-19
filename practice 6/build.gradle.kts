plugins {
    java
    id("nu.studer.jooq") version "8.0" apply false
}

group = "com.tsemkalo.homework6"
version = "1.0-SNAPSHOT"

subprojects {
    repositories {
        mavenCentral()
    }

    apply(plugin = "java")

    apply {
        plugin("nu.studer.jooq")
    }

    dependencies {
        implementation("org.jooq:jooq:3.17.5")
        implementation("org.jooq:jooq-codegen:3.17.5")
        implementation("org.jooq:jooq-meta:3.17.5")

        implementation("org.flywaydb:flyway-core:9.8.1")
        implementation("org.postgresql:postgresql:42.5.0")

        implementation("org.slf4j:slf4j-reload4j:2.0.3")
        implementation("org.slf4j:slf4j-api:2.0.3")

        implementation("com.google.code.gson:gson:2.10")
        implementation("com.google.inject:guice:5.1.0")

        implementation("org.projectlombok:lombok:1.18.24")
        annotationProcessor("org.projectlombok:lombok:1.18.24")
        implementation("com.intellij:annotations:12.0")

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
        implementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
        implementation("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }
}

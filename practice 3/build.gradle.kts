plugins {
    java
}

group = "com.tsemkalo.homework3"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "java")
    dependencies {
        implementation("com.google.inject:guice:5.1.0")
        implementation("org.projectlombok:lombok:1.18.24")
        annotationProcessor("org.projectlombok:lombok:1.18.24")
        implementation("com.intellij:annotations:12.0")
        implementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
        implementation("org.junit.jupiter:junit-jupiter-engine:5.9.0")
        testImplementation("org.mockito:mockito-junit-jupiter:4.8.0")
        testImplementation("org.mockito:mockito-inline:4.8.0")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
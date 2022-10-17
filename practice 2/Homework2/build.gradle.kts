plugins {
    java
}

group = "com.tsemkalo.homework2"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.intellij:annotations:12.0")
    implementation("org.slf4j:slf4j-api:2.0.3")
    implementation("org.slf4j:slf4j-reload4j:2.0.3")
//    implementation("org.apache.logging.log4j:log4j-core:2.3")
//    implementation("org.apache.logging.log4j:log4j-api:2.3")
//    compileOnly("org.apache.logging.log4j:log4j-api:2.15.0")
//    compileOnly("org.apache.logging.log4j:log4j-core:2.15.0")
//    compileOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.15.0")

    implementation("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    implementation("com.google.inject:guice:5.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
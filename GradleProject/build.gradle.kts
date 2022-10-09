plugins {
    java
}

group = "com.tsemkalo.library"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

//allprojects {
//    dependencies {
//        implementation("com.intellij:annotations:12.0")
//        implementation("org.projectlombok:lombok:1.18.24")
//        implementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
//        implementation("org.junit.jupiter:junit-jupiter-engine:5.9.0")
//    }
//}

dependencies {
//    annotationProcessor("org.projectlombok:lombok:1.18.24")
    implementation("com.intellij:annotations:12.0")
    implementation("org.junit.jupiter:junit-jupiter-api:5.9.0",)
    implementation("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

//dependencyManagement {
//    libraries = [
//        common: [
//    "com.intellij:annotations:12.0",
//    "org.projectlombok:lombok:1.18.24",
//    "org.junit.jupiter:junit-jupiter-api:5.9.0",
//    "org.junit.jupiter:junit-jupiter-engine:5.9.0"
//    ]
//    ]
//}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
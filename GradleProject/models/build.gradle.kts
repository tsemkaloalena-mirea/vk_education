group = "com.tsemkalo.library"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
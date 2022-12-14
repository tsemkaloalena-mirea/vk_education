group = "com.tsemkalo.homework8"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":dao"))
    implementation(project(":entities"))
    implementation(project(":controller"))
    implementation("org.jboss.resteasy:resteasy-guice:4.5.8.Final")
    implementation("org.jboss.resteasy:resteasy-jackson2-provider:4.5.8.Final")

}
group = "com.tsemkalo.homework6"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":dao"))
    implementation(project(":common"))
    implementation(project(":jooq-generated")) // TODO remove
}

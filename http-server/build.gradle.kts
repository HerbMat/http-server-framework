plugins {
    id("org.http.server.httpframework.module")
}

group = "org.http.server"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":logging"))
    implementation(project(":ioc-reflection"))
}

plugins {
    id("org.http.server.httpframework.module")
}

group = "org.http.server"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":logging"))
    implementation(project(":object-mapper"))
    api(project(":json-mapper"))
}

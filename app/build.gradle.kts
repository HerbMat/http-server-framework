plugins {
    id("org.http.server.httpframework.module")
    application
}

group = "org.http.server"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":logging"))
    implementation(project(":reflections"))
    implementation(project(":ioc-reflection"))
    implementation(project(":object-mapper"))
    implementation(project(":sse"))
    implementation(project(":http-server"))
    implementation(project(":json-mapper"))
}

application {
    mainClass.set("org.http.server.httpserverframework.MainKt")
}
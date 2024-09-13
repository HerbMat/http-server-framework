

plugins {
    id("org.http.server.httpframework.parent")
    id("org.http.server.context-indexer")
}

dependencies {
    api(project(":prototypes"))
}
plugins {
    id("org.http.server.httpframework.parent")
    `java-gradle-plugin`
    `maven-publish`
}

group = "org.http.server"
version = "1.0-SNAPSHOT"

gradlePlugin {
    plugins {
        create("context-indexer") {
            id = "org.http.server.context-indexer"
            implementationClass = "org.http.server.httpserverframework.task.ContextIndexerTask"
        }
    }
}

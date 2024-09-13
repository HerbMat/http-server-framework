plugins {
    id("org.http.server.httpframework.parent")
    `java-gradle-plugin`
    `maven-publish`
}

group = "org.http.server"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":prototypes"))
    implementation("org.ow2.asm:asm:9.2")
    implementation("org.ow2.asm:asm-tree:9.2")
}

gradlePlugin {
    plugins {
        create("context-indexer") {
            id = "org.http.server.context-indexer"
            implementationClass = "org.http.server.httpserverframework.task.ContextIndexerTask"
        }
    }
}

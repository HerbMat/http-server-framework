plugins {
    id("org.http.server.httpframework.parent")
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "org.http.server"
            artifactId = "prototypes"
            version = "1.0-SNAPSHOT"

            from(components["kotlin"])
        }
    }
}

group = "org.http.server"
version = "1.0-SNAPSHOT"

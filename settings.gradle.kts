rootProject.name = "http-server-framework"
include("context-indexer", "logging", "reflections", "ioc-reflection", "object-mapper", "json-mapper", "sse", "http-server", "app")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}

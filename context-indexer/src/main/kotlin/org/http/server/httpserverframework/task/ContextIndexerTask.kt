package org.http.server.httpserverframework.task

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.http.server.httpserverframework.task.indexer.ContextIndexer

abstract class ContextIndexerTask : Plugin<Project> {
    override fun apply(project: Project) {
        val contextIndexerTask = project.tasks.register("context-indexer") {
            it.doLast {
                println("Perform indexing LALO")
                val contextIndexer = ContextIndexer(project.buildDir.path)
                contextIndexer.createProjectIndex()
            }
        }
        project.tasks.named("build").configure {
            it.finalizedBy(contextIndexerTask)
        }
    }
}
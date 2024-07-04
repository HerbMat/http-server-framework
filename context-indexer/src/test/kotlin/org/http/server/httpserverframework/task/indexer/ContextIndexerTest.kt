package org.http.server.httpserverframework.task.indexer

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ContextIndexerTest : StringSpec({
    val contextIndexer = ContextIndexer("build")

    "it should return all classes" {
        contextIndexer.createProjectIndex()
    }

})

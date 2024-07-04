package org.http.server.httpserverframework.server.test.utils

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.shouldBe
import org.http.server.httpserverframework.server.test.utils.PathVariableMethods.isTemplatePart
import org.http.server.httpserverframework.server.test.utils.PathVariableMethods.removeTemplateBrackets

class PathVariableMethodsTest : StringSpec({
    "it should extract path variables successfully" {
        val requestPath = "/users/1/account/hello"
        val templatePath = "/users/{id}/account/{type}"

        val result = PathVariableMethods.extractPathVariables(requestPath, templatePath)

        result["id"] shouldBe  "1"
        result["type"] shouldBe "hello"
    }

    "it should throw exception for bad path length" {
        val requestPath = "/users/1/account"
        val templatePath = "/users/{id}/account/{type}"

        val exception = shouldThrow<RuntimeException> { PathVariableMethods.extractPathVariables(requestPath, templatePath) }

        exception.message shouldBe "Bad path"
    }

    "it should return properly resolve template path parts" {
        forAll(
            table(
                headers("pathPart", "expectedResult"),
                row("{id}", true),
                row("{id", false),
                row("id}", false),
                row("id", false)
            )
        ) { pathPart: String, expectedResult: Boolean ->
            pathPart.isTemplatePart() shouldBe expectedResult

        }

    }

    "it should strip string from path template brackets" {
        forAll(
            table(
                headers("pathPart", "expectedResult"),
                row("{id}", "id"),
                row("{id", "id"),
                row("id}", "id"),
                row("id", "id")
            )
        ) { pathPart: String, expectedResult: String ->
            pathPart.removeTemplateBrackets() shouldBe expectedResult
        }

    }

})

package org.http.server.httpserverframework.server.test.utils

import org.http.server.httpserverframework.log.Logger
import java.lang.RuntimeException

object PathVariableMethods {
    const val PATH_DELIMITER = "/"
    private const val TEMPLATE_PART_FIRST_LETTER = "{"
    private const val TEMPLATE_PART_LAST_LETTER = "}"
    private val log = Logger.getLogger(this::class.toString())

    fun extractPathVariables(requestPath: String, pathTemplate: String): Map<String, String> {
        log.debug("Resolve request path $requestPath for path template $pathTemplate")
        val pathVariableMap = mutableMapOf<String, String>()
        val splitPathTemplate = pathTemplate.split(PATH_DELIMITER)
        val splitRequestPath = requestPath.split(PATH_DELIMITER)
        if (splitPathTemplate.size > splitRequestPath.size) {
            log.error("Path is too short. It should have length ${splitPathTemplate.size} but it has ${splitRequestPath.size}")
            throw RuntimeException("Bad path")
        }
        for (i in splitPathTemplate.indices) {
            if (splitPathTemplate[i].isTemplatePart()) {
                pathVariableMap[splitPathTemplate[i].removeTemplateBrackets()] = splitRequestPath[i]
            }
        }
        return pathVariableMap.toMap()
    }

    fun String.isTemplatePart(): Boolean {
        log.debug("Checking if path part $this is template part")
        return this.startsWith(TEMPLATE_PART_FIRST_LETTER) && this.endsWith(
            TEMPLATE_PART_LAST_LETTER
        )
    }

    fun String.removeTemplateBrackets(): String {
        log.debug("Removing template brackets from template part $this")
        return this.removePrefix(TEMPLATE_PART_FIRST_LETTER).removeSuffix(TEMPLATE_PART_LAST_LETTER)
    }
}
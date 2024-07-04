package org.http.server.httpserverframework.mapper.json

import org.http.server.httpserverframework.log.Logger
import org.http.server.httpserverframework.mapper.ObjectMapper
import org.http.server.httpserverframework.mapper.util.FieldsExtractor
import java.lang.RuntimeException
import java.time.LocalDate
import java.util.regex.Pattern
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

class JsonMapper(private val fieldsExtractor: FieldsExtractor) : ObjectMapper {
    companion object {
        val INSTANCE by lazy {
            create()
        }

        private val EXTRACT_PATTERN = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"([^\"]+)\",?").toRegex()
        private val log = Logger.getLogger(this::class.toString())


        fun create(): JsonMapper {
            return JsonMapper(FieldsExtractor())
        }


    }

    override fun write(body: Any): String {
        if (log.isDebugEnabled()) {
            log.debug("Converting $body to json string")
        }
        val responseBody = fieldsExtractor.getFieldsWithValues(body)
            .map { " \"${it.key}\": \"${it.value}\"" }
            .joinToString(",")
        if (log.isDebugEnabled()) {
            log.debug("Converted $body to $responseBody")
        }
        return "{$responseBody}"
    }

    override fun write(body: List<Any>): String {
        if (log.isDebugEnabled()) {
            log.debug("Converting collection of objects ${body.joinToString { "|" }} to json array")
        }
        val jsonObjectBody = body.joinToString(",") { write(it) }
        if (log.isDebugEnabled()) {
            log.debug("Converted ${body.joinToString { "|" }} to $jsonObjectBody")
        }
        return "[$jsonObjectBody]"
    }

    override fun <T : Any> read(body: String, kClass: KClass<T>): T {
        log.debug("Converting $body to $kClass")
        val attributes = convertJsonToMap(body)
        if (log.isDebugEnabled()) {
            log.debug("Extracted attributes ${attributes.entries.joinToString()}")
        }
        val constructor = kClass.constructors.iterator().next()
        val parameters = constructor.parameters
        val constructorParameters = parameters.map { toParameter(it, attributes, it.type.classifier as KClass<*>) }

        return constructor.call(*constructorParameters.toTypedArray())
    }

    private fun convertJsonToMap(body: String): Map<String, String> {
        val keyValuePairs = EXTRACT_PATTERN.findAll(body).map { it.value }.toList()
        return keyValuePairs
            .map { it.trim() }
            .map { it.removeSuffix(",") }
            .map { el -> extractKeyAndValueFromJsonPair(el) }.associate { it[0] to it[1] }
    }

    private fun extractKeyAndValueFromJsonPair(jsonPair: String) =
        jsonPair.split(":").toList().map { it.trim() }.map { it.removePrefix("\"") }.map { it.removeSuffix("\"") }

    private fun <T : Any> toParameter(parameter: KParameter, attributes: Map<String, String>, type: KClass<T>): T {
        val resultParameter = attributes[parameter.name] ?: ""
        if (type == String::class) {
            return resultParameter as T
        }
        if (type == LocalDate::class) {
            return (LocalDate.parse(resultParameter) ?: LocalDate.now()) as T
        }
        throw RuntimeException("Bad type")
    }
}
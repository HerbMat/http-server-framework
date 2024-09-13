package org.http.server.httpserverframework.mapper.util

import org.http.server.httpserverframework.prototypes.Component
import kotlin.reflect.KProperty

@Component
class FieldsExtractor {
    fun getFieldsWithValues(body: Any): Map<String, String> {
        return body::class.members.filter { it is KProperty }.associate { it.name to it.call(body).toString() }
    }
}
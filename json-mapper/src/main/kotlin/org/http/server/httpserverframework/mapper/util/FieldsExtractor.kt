package org.http.server.httpserverframework.mapper.util

import kotlin.reflect.KProperty

class FieldsExtractor {
    fun getFieldsWithValues(body: Any): Map<String, String> {
        return body::class.members.filter { it is KProperty }.associate { it.name to it.call(body).toString() }
    }
}
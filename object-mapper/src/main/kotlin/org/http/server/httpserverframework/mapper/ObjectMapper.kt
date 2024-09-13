package org.http.server.httpserverframework.mapper

import kotlin.reflect.KClass

interface ObjectMapper {
    fun write(body: Any): String
    fun write(body: List<Any>): String
    fun <T : Any> read(body: String, kClass: KClass<T>): T
}
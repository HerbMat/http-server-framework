package org.http.server.httpserverframework.bootstrap

class ServerContext {
    val loadedObjects: MutableMap<Class<*>, Any> = mutableMapOf()
}
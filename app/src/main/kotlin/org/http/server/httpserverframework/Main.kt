package org.http.server.httpserverframework

import org.http.server.httpserverframework.bind.annotation.Controller
import org.http.server.httpserverframework.bootstrap.BootstrapLoader
import org.http.server.httpserverframework.bootstrap.reflection.ReflectionUtils
import org.http.server.httpserverframework.handlers.RequestHandler
import org.http.server.httpserverframework.handlers.methods.RequestHandlerMethod

fun main(args: Array<String>) {
    val bootstrapLoader = BootstrapLoader("org.http.server.httpserverframework")
    val serverContext = bootstrapLoader.prepareContext(mainClassAnnotation = Controller::class.java)
    val controllers = serverContext.loadedObjects.filter { ReflectionUtils.hasAnnotation(it.key, Controller::class.java) }
        .values
        .map { it as RequestHandlerMethod }
        .toList()

    val requestHandler = RequestHandler.create(controllers)
    val serverApp = ServerApp.create(requestHandler)
    serverApp.start()
}
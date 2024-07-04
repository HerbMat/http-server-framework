package org.http.server.httpserverframework.app

import org.http.server.httpserverframework.server.bind.annotation.Controller
import org.http.server.httpserverframework.bootstrap.BootstrapLoader
import org.http.server.httpserverframework.reflection.ReflectionUtils
import org.http.server.httpserverframework.server.ServerApp
import org.http.server.httpserverframework.server.handlers.RequestHandler
import org.http.server.httpserverframework.server.handlers.methods.RequestHandlerMethod

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
//    Arrays.stream(TestContainersApplication.class.getClassLoader().getDefinedPackages())
//        .filter(it -> it.getName().contains("spring"))
//    .collect(Collectors.toList())
}
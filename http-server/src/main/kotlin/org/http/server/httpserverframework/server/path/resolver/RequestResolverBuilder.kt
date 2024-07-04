package org.http.server.httpserverframework.server.path.resolver

import org.http.server.httpserverframework.server.handlers.methods.RequestHandlerMethod
import org.http.server.httpserverframework.log.Logger
import org.http.server.httpserverframework.server.test.utils.PathVariableMethods

class RequestResolverBuilder {

    companion object {
        private val log = Logger.getLogger(this::class.toString())
    }
    private val rootElement = RequestMethodResolverNodeBuilder( 0)
    fun addRequestHandlerMethod(requestHandlerMethod: RequestHandlerMethod) {
        log.debug("Adding handler for path ${requestHandlerMethod.path} and method ${requestHandlerMethod.method}")
        val splitPath = requestHandlerMethod.path.split(PathVariableMethods.PATH_DELIMITER).filter { it.isNotBlank() }
        rootElement.addRequestHandlerMethod(requestHandlerMethod, splitPath)
    }

    fun build(): RequestResolver {
        return RequestResolver(rootElement.build())
    }
}
package org.http.server.httpserverframework.server.path.resolver

import org.http.server.httpserverframework.server.handlers.methods.RequestHandlerMethod
import org.http.server.httpserverframework.log.Logger
import org.http.server.httpserverframework.server.test.utils.PathVariableMethods.PATH_DELIMITER
import java.net.URI

class RequestResolver(private val rootRequestMethodResolverNode: RequestMethodResolverNode) {
    companion object {
        private val log = Logger.getLogger(this::class.toString())

        fun create(requestHandlerMethods: List<RequestHandlerMethod>): RequestResolver {
            log.debug("Creating request resolver")
            val requestResolverBuilder = RequestResolverBuilder()
            requestHandlerMethods.forEach { requestResolverBuilder.addRequestHandlerMethod(it) }

            return requestResolverBuilder.build()
        }
    }

    fun resolve(uri: URI, method: String): RequestHandlerMethod? {
        log.debug("Looking for request handler method for uri $uri and method $method")
        val splitUri = uri.path.split(PATH_DELIMITER).filter { it.isNotBlank() }
        return rootRequestMethodResolverNode.resolve(splitUri, method)
    }

    fun availableMethods(uri: URI): Set<String> {
        log.debug("Retrieving available methods for uri $uri")
        val splitUri = uri.path.split(PATH_DELIMITER).filter { it.isNotBlank() }
        val availableMethods = rootRequestMethodResolverNode.availableMethods(splitUri)
        if (log.isDebugEnabled()) {
            log.debug("For uri $uri returned ${availableMethods.joinToString() }")
        }
        return availableMethods
    }
}
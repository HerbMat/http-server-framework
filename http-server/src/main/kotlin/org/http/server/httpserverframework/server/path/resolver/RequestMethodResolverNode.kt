package org.http.server.httpserverframework.server.path.resolver

import org.http.server.httpserverframework.server.handlers.methods.RequestHandlerMethod
import org.http.server.httpserverframework.log.Logger

class RequestMethodResolverNode(
    private val depth: Int,
    private val namedRequestMethodResolverNodeElements: Map<String, RequestMethodResolverNode>,
    private val placeHolderRequestMethodResolverNode: RequestMethodResolverNode?,
    private val requestResolvers: Map<String, RequestHandlerMethod>
) {
    companion object {
        private val log = Logger.getLogger(this::class.toString())
    }

    fun resolve(splitUri: List<String>, method: String): RequestHandlerMethod? {
        if (depth == splitUri.size) {
            if (log.isDebugEnabled()) {
                log.debug("Found handler for ${splitUri.joinToString { "/" }} and method $method")
            }
            return requestResolvers[method]
        }
        return getTrieForRequestUri(splitUri[depth])?.resolve(splitUri, method)
    }

    fun availableMethods(splitUri: List<String>): Set<String> {
        if (depth == splitUri.size) {
            if (log.isDebugEnabled()) {
                log.debug("Returning available methods for ${splitUri.joinToString { "/" }}")
            }
            return requestResolvers.keys
        }
        return getTrieForRequestUri(splitUri[depth])?.availableMethods(splitUri) ?: setOf()
    }

    private fun getTrieForRequestUri(pathPart: String): RequestMethodResolverNode? {
        return namedRequestMethodResolverNodeElements[pathPart] ?: placeHolderRequestMethodResolverNode
    }
}
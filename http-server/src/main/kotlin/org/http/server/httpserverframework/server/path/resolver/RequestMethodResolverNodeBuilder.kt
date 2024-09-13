package org.http.server.httpserverframework.server.path.resolver

import org.http.server.httpserverframework.server.handlers.methods.RequestHandlerMethod
import org.http.server.httpserverframework.server.test.utils.PathVariableMethods.isTemplatePart

class RequestMethodResolverNodeBuilder(private val depth: Int) {
    private val namedRequestMethodTrieElements: MutableMap<String, RequestMethodResolverNodeBuilder> = mutableMapOf()
    private var placeHolderRequestMethodTrieElement: RequestMethodResolverNodeBuilder? = null
    private val requestResolvers: MutableMap<String, RequestHandlerMethod> = mutableMapOf()

    fun build(): RequestMethodResolverNode {
        return RequestMethodResolverNode(
            depth,
            namedRequestMethodTrieElements.map { it.key to it.value.build() }.toMap(),
            placeHolderRequestMethodTrieElement?.build(),
            requestResolvers
        )
    }

    fun addRequestHandlerMethod(requestHandlerMethod: RequestHandlerMethod, splitPath: List<String>) {
        if (depth == splitPath.size) {
            requestResolvers[requestHandlerMethod.method] = requestHandlerMethod
        } else {
            addToChildTrie(requestHandlerMethod, splitPath)
        }
    }

    private fun addToChildTrie(requestHandlerMethod: RequestHandlerMethod, splitPath: List<String>) {
        val trieElement = getChildTrieForPathElement(splitPath[depth])
        trieElement.addRequestHandlerMethod(requestHandlerMethod, splitPath)
    }

    private fun getChildTrieForPathElement(pathElement: String): RequestMethodResolverNodeBuilder {
        if (pathElement.isTemplatePart()) {
            if (placeHolderRequestMethodTrieElement == null) {
                placeHolderRequestMethodTrieElement = RequestMethodResolverNodeBuilder( depth + 1)
            }
            return placeHolderRequestMethodTrieElement!!
        }
        return namedRequestMethodTrieElements.computeIfAbsent(pathElement) {
            RequestMethodResolverNodeBuilder(depth + 1)
        }
    }
}

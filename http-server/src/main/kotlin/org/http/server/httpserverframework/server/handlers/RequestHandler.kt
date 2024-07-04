package org.http.server.httpserverframework.server.handlers

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import org.http.server.httpserverframework.server.handlers.methods.RequestHandlerMethod
import org.http.server.httpserverframework.server.handlers.methods.impl.NotFoundHttpMethod
import org.http.server.httpserverframework.server.handlers.methods.impl.OptionsHttpMethod
import org.http.server.httpserverframework.server.http.HttpMethod
import org.http.server.httpserverframework.log.Logger
import org.http.server.httpserverframework.server.path.resolver.RequestResolver

class RequestHandler(
    private val requestResolver: RequestResolver,
    private val optionsHttpMethod: OptionsHttpMethod,
    private val notFoundHttpMethod: NotFoundHttpMethod
) : HttpHandler {
    companion object {
        const val PATH = "/"
        const val NOT_FOUND_MESSAGE = "Not Found"

        private val log = Logger.getLogger(this::class.toString())

        fun create(handlerMethods: List<RequestHandlerMethod>): RequestHandler {
            log.debug("Creating Request Handler class")
            return RequestHandler(RequestResolver.create(handlerMethods), OptionsHttpMethod(), NotFoundHttpMethod())
        }
    }

    override fun handle(httpExchange: HttpExchange) {
        if (log.isDebugEnabled()) {
            log.debug("Received request $httpExchange")
        }
        if (httpExchange.requestMethod == HttpMethod.OPTIONS) {
            log.debug("Got Options Request with path ${httpExchange.requestURI}")
            optionsHttpMethod.handle(httpExchange, requestResolver.availableMethods(httpExchange.requestURI))
        } else {
            log.debug("Handler for uri ${httpExchange.requestURI} and method ${httpExchange.requestMethod} not found falling back to not found handler.")
            requestResolver.resolve(httpExchange.requestURI, httpExchange.requestMethod)?.handle(httpExchange)
                ?: run {
                    notFoundHttpMethod.handle(httpExchange)
                }
        }
    }
}

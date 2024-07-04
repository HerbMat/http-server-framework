package org.http.server.httpserverframework.server.handlers.methods.impl

import com.sun.net.httpserver.HttpExchange
import org.http.server.httpserverframework.server.handlers.methods.RequestHandlerMethod
import org.http.server.httpserverframework.server.http.HttpHeaders
import org.http.server.httpserverframework.log.Logger
import org.http.server.httpserverframework.server.test.utils.HttpExchangeExtensions.addCORSHeaders

class OptionsHttpMethod: RequestHandlerMethod() {
    companion object {
        private val log = Logger.getLogger(this::class.toString())
        val INSTANCE by lazy {
            OptionsHttpMethod()
        }
    }

    override fun handle(httpExchange: HttpExchange) {
        log.debug("Received request with uri ${httpExchange.requestURI} and method ${httpExchange.requestMethod}")
        httpExchange.addCORSHeaders()
        httpExchange.sendResponseHeaders(200, 0)
    }

    fun handle(httpExchange: HttpExchange, availableMethods: Set<String>) {
        httpExchange.responseHeaders.add(HttpHeaders.ALLOW, availableMethods.joinToString { "," })
        this.handle(httpExchange)
    }
}
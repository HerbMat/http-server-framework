package org.http.server.httpserverframework.server.handlers.methods.impl

import com.sun.net.httpserver.HttpExchange
import org.http.server.httpserverframework.server.handlers.RequestHandler
import org.http.server.httpserverframework.server.handlers.methods.RequestHandlerMethod
import org.http.server.httpserverframework.server.http.HttpCodes
import org.http.server.httpserverframework.log.Logger

class NotFoundHttpMethod: RequestHandlerMethod() {
    companion object {
        private val log = Logger.getLogger(this::class.toString())
        val INSTANCE by lazy {
            NotFoundHttpMethod()
        }
    }

    override fun handle(httpExchange: HttpExchange) {
        log.debug("Received request with uri ${httpExchange.requestURI} and method ${httpExchange.requestMethod}")

        httpExchange.sendResponseHeaders(HttpCodes.NOT_FOUND, RequestHandler.NOT_FOUND_MESSAGE.length.toLong())
        httpExchange.responseBody.use {
            it.write(RequestHandler.NOT_FOUND_MESSAGE.toByteArray())
        }
    }
}
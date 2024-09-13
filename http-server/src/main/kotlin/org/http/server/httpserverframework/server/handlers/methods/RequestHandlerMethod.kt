package org.http.server.httpserverframework.server.handlers.methods

import com.sun.net.httpserver.HttpExchange
import org.http.server.httpserverframework.server.http.HttpMethod

abstract class RequestHandlerMethod {
    open val path: String = "/"
    open val method: String = HttpMethod.GET

    abstract fun handle(httpExchange: HttpExchange)
}
package org.http.server.httpserverframework.server.test.utils

import com.sun.net.httpserver.HttpExchange
import org.http.server.httpserverframework.server.http.HttpHeaders
import org.http.server.httpserverframework.server.http.MediaType
import org.http.server.httpserverframework.log.Logger

object HttpExchangeExtensions {
    private val log = Logger.getLogger(this::class.toString())

    fun HttpExchange.addEventSourcingHeaders() {
        log.debug("Adding ${HttpHeaders.CONTENT_TYPE} and ${HttpHeaders.CONNECTION} and ${HttpHeaders.X_POWERED_BY} headers to response")
        this.responseHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM)
        this.responseHeaders.add(HttpHeaders.CONNECTION, "keep-alive")
        this.responseHeaders.add(HttpHeaders.X_POWERED_BY, "Native Application Server")
    }

    fun HttpExchange.addCORSHeaders() {
        log.debug("Adding ${HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS} and ${HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS} and ${HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN} headers to response")
        this.responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
        this.responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        this.responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
    }
}
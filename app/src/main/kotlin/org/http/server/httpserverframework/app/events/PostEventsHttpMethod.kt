package org.http.server.httpserverframework.app.events

import com.sun.net.httpserver.HttpExchange
import org.http.server.httpserverframework.server.bind.annotation.Controller
import org.http.server.httpserverframework.app.db.EventFileRepository
import org.http.server.httpserverframework.app.db.EventRepository
import org.http.server.httpserverframework.app.domain.Event
import org.http.server.httpserverframework.server.handlers.methods.RequestHandlerMethod
import org.http.server.httpserverframework.server.http.HttpCodes
import org.http.server.httpserverframework.server.http.HttpMethod
import org.http.server.httpserverframework.log.Logger
import org.http.server.httpserverframework.mapper.ObjectMapper
import org.http.server.httpserverframework.mapper.json.JsonMapper
import org.http.server.httpserverframework.server.test.utils.HttpExchangeExtensions.addCORSHeaders
import java.util.stream.Collectors

@Controller(path = "/notifications")
class PostEventsHttpMethod(
    private val eventRepository: EventRepository,
    private val objectMapper: ObjectMapper
): RequestHandlerMethod() {
    override val path = "/notifications"
    override val method = HttpMethod.POST

    companion object {
        val INSTANCE by lazy {
            create()
        }
        private val log = Logger.getLogger(this::class.toString())
        fun create() = PostEventsHttpMethod(EventFileRepository.INSTANCE, JsonMapper.INSTANCE)
    }

    override fun handle(httpExchange: HttpExchange) {
        httpExchange.addCORSHeaders()
        val requestBody = httpExchange.requestBody.bufferedReader().use {
            it.lines().collect(Collectors.toList()).toList().joinToString(" ")
        }
        log.debug("Received request with request body $requestBody")
        val event = objectMapper.read(requestBody, Event::class)
        eventRepository.add(event)
        log.debug("Successfully added event $event")
        httpExchange.sendResponseHeaders(HttpCodes.CREATED, 0)
        httpExchange.close()
    }
}
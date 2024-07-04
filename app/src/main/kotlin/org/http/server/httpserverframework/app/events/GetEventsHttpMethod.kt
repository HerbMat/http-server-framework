package org.http.server.httpserverframework.app.events

import com.sun.net.httpserver.HttpExchange
import org.http.server.httpserverframework.server.bind.annotation.Controller
import org.http.server.httpserverframework.converters.SseResponseConverter
import org.http.server.httpserverframework.converters.sse.SseEvent
import org.http.server.httpserverframework.converters.sse.SseEvents
import org.http.server.httpserverframework.app.db.EventRepository
import org.http.server.httpserverframework.app.domain.Event
import org.http.server.httpserverframework.server.handlers.methods.RequestHandlerMethod
import org.http.server.httpserverframework.server.http.HttpCodes
import org.http.server.httpserverframework.server.http.HttpHeaders
import org.http.server.httpserverframework.log.Logger
import org.http.server.httpserverframework.server.test.utils.HttpExchangeExtensions.addCORSHeaders
import org.http.server.httpserverframework.server.test.utils.HttpExchangeExtensions.addEventSourcingHeaders

@Controller(path = "/notifications/sse")
class GetEventsHttpMethod(
    private val eventRepository: EventRepository,
    private val sseResponseConverter: SseResponseConverter
): RequestHandlerMethod() {
    override val path = "/notifications/sse"

    companion object {
        private val log = Logger.getLogger(this::class.toString())
    }

    override fun handle(httpExchange: HttpExchange) {
        log.debug("Received request to retrieve all events")
        httpExchange.addEventSourcingHeaders()
        httpExchange.addCORSHeaders()
        httpExchange.sendResponseHeaders(HttpCodes.OK, 0)
        val events = eventRepository.findAll()
        val nextKey = retrieveNextEventId(httpExchange)
        log.debug("Last key is $nextKey")
        events.drop(nextKey.toInt() - 1).toList().let {
            addEventToResponse(httpExchange, it)
        }
        httpExchange.close()
    }

    private fun addEventToResponse(httpExchange: HttpExchange, events: List<Event>) {
        val eventResponse = sseResponseConverter.convertToResponse(SseEvents("test-event", events.map { SseEvent(it.id, it) }))
        log.debug("Returning response with body $eventResponse")
        httpExchange.responseBody.use {
            it.write(eventResponse.toByteArray())
        }
    }

    private fun retrieveNextEventId(httpExchange: HttpExchange): String {
        if (httpExchange.requestHeaders.containsKey(HttpHeaders.LAST_EVENT_ID)) {
            val prevId = httpExchange.requestHeaders[HttpHeaders.LAST_EVENT_ID]?.get(0)?.toInt() ?: 0

            return (prevId.plus(1)).toString()
        }
        return "1"
    }
}
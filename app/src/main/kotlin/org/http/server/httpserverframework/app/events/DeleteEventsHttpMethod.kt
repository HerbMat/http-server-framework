package org.http.server.httpserverframework.app.events

import com.sun.net.httpserver.HttpExchange
import org.http.server.httpserverframework.server.bind.annotation.Controller
import org.http.server.httpserverframework.app.db.EventRepository
import org.http.server.httpserverframework.server.handlers.methods.RequestHandlerMethod
import org.http.server.httpserverframework.server.http.HttpCodes
import org.http.server.httpserverframework.server.http.HttpMethod
import org.http.server.httpserverframework.log.Logger
import org.http.server.httpserverframework.server.test.utils.HttpExchangeExtensions.addCORSHeaders
import org.http.server.httpserverframework.server.test.utils.PathVariableMethods

@Controller(path = "/notifications/{id}")
class DeleteEventsHttpMethod(
    private val eventRepository: EventRepository,
): RequestHandlerMethod() {
    override val path = "/notifications/{$PATH_ID}"
    override val method = HttpMethod.DELETE

    companion object {
        private val log = Logger.getLogger(this::class.toString())
        private const val PATH_ID = "id"
    }

    override fun handle(httpExchange: HttpExchange) {
        val pathVariables = PathVariableMethods.extractPathVariables(httpExchange.requestURI.toString(), path)
        pathVariables[PATH_ID]?.also {
            log.debug("Received request to delete event with id $it")
            eventRepository.deleteById(it)
            log.debug("Deleted event with id $it")
        }
        httpExchange.addCORSHeaders()
        httpExchange.sendResponseHeaders(HttpCodes.NO_CONTENT, 0)
        httpExchange.close()
    }
}
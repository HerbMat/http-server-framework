package org.http.server.httpserverframework.handlers

import com.sun.net.httpserver.HttpExchange
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import org.http.server.httpserverframework.handlers.methods.impl.events.DeleteEventsHttpMethod
import org.http.server.httpserverframework.handlers.methods.impl.events.GetEventsHttpMethod
import org.http.server.httpserverframework.handlers.methods.impl.events.PostEventsHttpMethod
import java.net.URI

class RequestHandlerTest : StringSpec({
    val httpExchange = mockk<HttpExchange>()
    "It decodes path correctly" {
        val requestHandler = RequestHandler.create(listOf(GetEventsHttpMethod.create(), PostEventsHttpMethod.create(), DeleteEventsHttpMethod.create()))
        every { httpExchange.requestURI } returns URI.create("/notifications/3")

        requestHandler.handle(httpExchange)
    }

})

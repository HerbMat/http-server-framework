package org.http.server.httpserverframework.converters

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.http.server.httpserverframework.converters.sse.SseEvent
import org.http.server.httpserverframework.converters.sse.SseEvents
import org.http.server.httpserverframework.converters.test.utils.EventFactory
import org.http.server.httpserverframework.mapper.json.JsonMapper
import org.http.server.httpserverframework.mapper.util.FieldsExtractor

class SseResponseConverterTest : StringSpec({
    val sseResponseConverter = SseResponseConverter(JsonMapper(FieldsExtractor()))

    "Get body list should be converted to sse response body" {
        val events = EventFactory.createEvents()
        val sseEvent1 = SseEvent("1", events[0])
        val sseEvent2 = SseEvent("2", events[1])
        val response = sseResponseConverter.convertToResponse(SseEvents("test-event", listOf(sseEvent1, sseEvent2)))

        val expectedResponse = """
        event: test-event
        id: 2
        data: { "eventDate": "2020-05-07", "id": "1", "title": "Title1"}
        data: { "eventDate": "2020-05-08", "id": "2", "title": "Title2"}


        """.trimIndent()

        response shouldNotBe null
        response shouldBe expectedResponse
    }

    "Get body list should return empty response if there are no elements" {
        val response = sseResponseConverter.convertToResponse(SseEvents("test-event", listOf()))

        response shouldNotBe null
        response shouldBe "\n\n"
    }

})

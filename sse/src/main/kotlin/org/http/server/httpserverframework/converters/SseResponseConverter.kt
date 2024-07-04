package org.http.server.httpserverframework.converters

import org.http.server.httpserverframework.converters.sse.SseEvents
import org.http.server.httpserverframework.log.Logger
import org.http.server.httpserverframework.mapper.ObjectMapper

class SseResponseConverter(private val responseMapper: ObjectMapper) {
    companion object {
        private val log = Logger.getLogger(this::class.toString())
    }

    fun convertToResponse(sseEvents: SseEvents): String {
        if (log.isDebugEnabled()) {
            log.debug("Converting events to sse response body $sseEvents")
        }
        if (sseEvents.sseEventList.isEmpty()) {
            return "\n\n"
        }
        val eventsResponsePayload = sseEvents.sseEventList.joinToString("\n") { "data: ${responseMapper.write(it.body)}" }
        val lastEvent = sseEvents.sseEventList.last()
        val responseBody = """
                event: ${sseEvents.name}
                id: ${lastEvent.id}
            """.trimIndent()

        return "$responseBody\n$eventsResponsePayload\n\n"
    }
}

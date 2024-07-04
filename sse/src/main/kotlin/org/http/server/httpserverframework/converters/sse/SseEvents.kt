package org.http.server.httpserverframework.converters.sse

data class SseEvents(val name: String, val sseEventList: List<SseEvent>)

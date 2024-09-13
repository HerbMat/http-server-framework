package org.http.server.httpserverframework.converters.sse

data class SseEvent(val id: String, val body: Any)

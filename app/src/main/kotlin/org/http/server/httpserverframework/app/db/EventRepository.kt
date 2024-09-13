package org.http.server.httpserverframework.app.db

import org.http.server.httpserverframework.app.domain.Event

interface EventRepository {
    fun findAll(): List<Event>
    fun add(event: Event): Event
    fun deleteById(id: String)
}
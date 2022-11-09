package org.http.server.httpserverframework.db

import org.http.server.httpserverframework.domain.Event

interface EventRepository {
    fun findAll(): List<Event>
    fun add(event: Event): Event
    fun deleteById(id: String)
}
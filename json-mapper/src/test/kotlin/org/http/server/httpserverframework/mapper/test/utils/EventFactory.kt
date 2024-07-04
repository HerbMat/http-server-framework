package org.http.server.httpserverframework.mapper.test.utils

import java.time.LocalDate

object EventFactory {
    fun createEvent(): Event {
        return Event("1", LocalDate.of(2020,5, 7), "Title")
    }

    fun createEvents(): List<Event> {
        return listOf(
            Event("1", LocalDate.of(2020,5, 7), "Title1"),
            Event("2", LocalDate.of(2020,5, 8), "Title2"),
        )
    }
}
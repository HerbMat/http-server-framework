package org.http.server.httpserverframework.app.db

import io.kotest.core.spec.style.StringSpec

internal class EventFileRepositoryTest : StringSpec({

//    val databaseFile = Path.of(Thread.currentThread().contextClassLoader.getResource(EventFileRepository.DATABASE_FILE)?.path ?: "")
//    val originalFile = Path.of("original_database.csv")
//
//    val eventRepository = EventFileRepository(databaseFile)
//
//    beforeEach {
//        Files.copy(databaseFile, originalFile)
//    }
//
//    afterEach {
//        Files.delete(databaseFile)
//        Files.move(originalFile, databaseFile)
//    }
//    "It should return two events" {
//        val events = eventRepository.findAll()
//        events shouldHaveSize 3
//    }
//
//    "It should add new entry to database" {
//
//        val newEvent = Event("4", LocalDate.now(), "New event")
//
//        eventRepository.add(newEvent)
//        val events = eventRepository.findAll()
//
//        events shouldHaveSize 4
//        events[3] shouldBeEqualToComparingFields newEvent
//    }
//
//    "It should delete event with id 1" {
//        eventRepository.deleteById("1")
//        val events = eventRepository.findAll()
//
//        events shouldHaveSize 2
//        events.forAll { it.id shouldNotBe "1" }
//    }
})
package org.http.server.httpserverframework.app.db

import org.http.server.httpserverframework.app.domain.Event
import org.http.server.httpserverframework.log.Logger
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EventFileRepository private constructor(private val path: Path): EventRepository {
    companion object {
        val INSTANCE by lazy {
            create()
        }

        private val log = Logger.getLogger(this::class.toString())
        private const val DATABASE_FILE = "database.csv"
        const val DELIMITER = ";"
        fun create(): EventFileRepository {
            return EventFileRepository()
        }
    }

    constructor() : this(Path.of(DATABASE_FILE))

    override fun findAll(): List<Event> {
        log.debug("Retrieving all events")
        return Files.readAllLines(path).drop(1).map { line -> readEventRow(line) }
    }

    override fun add(event: Event): Event {
        log.debug("Adding new event $event")
        saveAllEvents(readAllEventRecords() + convertToRecord(event))
        log.debug("Added event $event")

        return event
    }

    override fun deleteById(id: String) {
        log.debug("Deleting event with id $id")
        saveAllEvents(readAllEventRecords().filter { !it.startsWith("$id;") })
        log.debug("Deleted event with id $id")
    }

    private fun readEventRow(line: String): Event {
        val splitLine = line.split(DELIMITER)
        return Event(splitLine[0], LocalDate.parse(splitLine[2], DateTimeFormatter.ISO_LOCAL_DATE), splitLine[1])
    }

    private fun convertToRecord(event: Event): String {
        return "${event.id};${event.title};${event.eventDate.format(DateTimeFormatter.ISO_LOCAL_DATE)}"
    }

    private fun readAllEventRecords(): List<String> {
        return Files.readAllLines(path)
    }

    private fun saveAllEvents(records: List<String>) {
        path.toFile().printWriter().use {
                out -> records.forEach { out.println(it) }
        }
    }

}
package org.http.server.httpserverframework.mapper.json

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.http.server.httpserverframework.mapper.test.utils.Event
import org.http.server.httpserverframework.mapper.test.utils.EventFactory
import org.http.server.httpserverframework.mapper.util.FieldsExtractor
import java.time.LocalDate

class JsonMapperTest : StringSpec({
    val jsonMapper = JsonMapper(FieldsExtractor())
    "It should return objects as json object string" {
        val result = jsonMapper.write(EventFactory.createEvent())

        result shouldNotBe null
        result shouldBe "{ \"eventDate\": \"2020-05-07\", \"id\": \"1\", \"title\": \"Title\"}"
    }

    "It should return list of objects as json array string" {
        val result = jsonMapper.write(EventFactory.createEvents())

        result shouldNotBe null
        result shouldBe "[{ \"eventDate\": \"2020-05-07\", \"id\": \"1\", \"title\": \"Title1\"},{ \"eventDate\": \"2020-05-08\", \"id\": \"2\", \"title\": \"Title2\"}]"
    }

    "It should extract json pairs" {
        val body = """
            { "id": "5", "title": "test", "eventDate": "2041-05-03" }
        """.trimIndent()
        val eventObj = jsonMapper.read(body, Event::class)
        eventObj.id shouldBe "5"
        eventObj.title shouldBe "test"
        eventObj.eventDate shouldBe LocalDate.of(2041, 5, 3)
    }

})

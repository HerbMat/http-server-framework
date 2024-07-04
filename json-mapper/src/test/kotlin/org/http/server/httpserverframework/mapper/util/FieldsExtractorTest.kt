package org.http.server.httpserverframework.mapper.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.maps.shouldHaveKey
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.http.server.httpserverframework.mapper.test.utils.EventFactory

class FieldsExtractorTest : StringSpec({
    val fieldsExtractor = FieldsExtractor()
    "it should return event as string" {
        val event = EventFactory.createEvent()
        val result = fieldsExtractor.getFieldsWithValues(event)
        result shouldHaveSize 3
        result shouldHaveKey "id"
        result shouldHaveKey "title"
        result shouldHaveKey "eventDate"
        result["id"] shouldBe "1"
        result["title"] shouldBe "Title"
        result["eventDate"] shouldBe event.eventDate.toString()

    }
})

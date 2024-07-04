package org.http.server.httpserverframework.server.path.resolver

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.http.server.httpserverframework.server.http.HttpMethod
import org.http.server.httpserverframework.server.test.utils.MockRequestMethodFactory
import java.net.URI

class RequestResolverTest : StringSpec({
    val requestMethodResolverNodeMock = mockk<RequestMethodResolverNode>()
    every { requestMethodResolverNodeMock.resolve(any(), any()) } returns MockRequestMethodFactory.mainMethod
    every { requestMethodResolverNodeMock.availableMethods(any()) } returns setOf(HttpMethod.GET, HttpMethod.DELETE)
    val requestResolverWithMock = RequestResolver(requestMethodResolverNodeMock)

    "it should properly split uri" {
        val testUri = URI.create("/users/1/test")
        val result = requestResolverWithMock.resolve(testUri, HttpMethod.PUT)

        result shouldBe MockRequestMethodFactory.mainMethod
        verify {
            requestMethodResolverNodeMock.resolve(match { splitPath -> splitPath == listOf("users", "1", "test") },
            match{ method -> method == HttpMethod.PUT })
        }
    }

    "it should properly split empty uri" {
        val testUri = URI.create("/")
        val result = requestResolverWithMock.resolve(testUri, HttpMethod.DELETE)

        result shouldBe MockRequestMethodFactory.mainMethod
        verify {
            requestMethodResolverNodeMock.resolve(match { splitPath -> splitPath == listOf<String>() },
                match{ method -> method == HttpMethod.DELETE })
        }
    }

    "it should return available methods correctly" {
        val testUri = URI.create("/users")
        val result = requestResolverWithMock.availableMethods(testUri)

        result shouldBe setOf(HttpMethod.GET, HttpMethod.DELETE)
        verify {
            requestMethodResolverNodeMock.availableMethods(match { splitPath -> splitPath == listOf("users") })
        }
    }

    val requestResolver = RequestResolver.create(MockRequestMethodFactory.mockHttpMethodList)

    "it should return address user method" {
        val requestUri = URI.create("/users/1/account/2")
        val result = requestResolver.resolve(requestUri, HttpMethod.GET)

        result shouldBe MockRequestMethodFactory.getUserAccountMethod
    }

    "it should return main handler method" {
        val requestUri = URI.create("/")
        val result = requestResolver.resolve(requestUri, HttpMethod.GET)

        result shouldBe MockRequestMethodFactory.mainMethod
    }

    "it should return null for not existing method" {
        val requestUri = URI.create("/notexisting")
        val result = requestResolver.resolve(requestUri, HttpMethod.DELETE)

        result shouldBe null
    }

    "it should return notifications sse method" {
        val requestUri = URI.create("/notifications/sse")
        val result = requestResolver.resolve(requestUri, HttpMethod.GET)

        result shouldBe MockRequestMethodFactory.getNotificationsSseMethod
    }

    "it should return post users method" {
        val requestUri = URI.create("/users")
        val result = requestResolver.resolve(requestUri, HttpMethod.POST)

        result shouldBe MockRequestMethodFactory.postUserMethod
    }

})

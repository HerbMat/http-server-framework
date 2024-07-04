package org.http.server.httpserverframework.server.test.utils

import com.sun.net.httpserver.HttpExchange
import org.http.server.httpserverframework.server.handlers.methods.RequestHandlerMethod
import org.http.server.httpserverframework.server.http.HttpMethod

object MockRequestMethodFactory {
    val mainMethod = object: RequestHandlerMethod() {
        override fun handle(httpExchange: HttpExchange) {

        }
    }

    val getNotificationsMethod = object: RequestHandlerMethod() {
        override val path: String
            get() = "/notifications/"
        override fun handle(httpExchange: HttpExchange) {

        }
    }

    val getNotificationsSseMethod = object: RequestHandlerMethod() {
        override val path: String
            get() = "/notifications/sse"
        override fun handle(httpExchange: HttpExchange) {

        }
    }

    val postNotificationMethod = object: RequestHandlerMethod() {
        override val path: String
            get() = "/notifications/"
        override val method: String
            get() = HttpMethod.POST
        override fun handle(httpExchange: HttpExchange) {

        }
    }

    val getNotificationMethod = object: RequestHandlerMethod() {
        override val path: String
            get() = "/notifications/{id}"
        override fun handle(httpExchange: HttpExchange) {

        }
    }

    val postUserMethod = object: RequestHandlerMethod() {
        override val path: String
            get() = "/users/"
        override val method: String
            get() = HttpMethod.POST
        override fun handle(httpExchange: HttpExchange) {

        }
    }

    val putUserMethod = object: RequestHandlerMethod() {
        override val path: String
            get() = "/users/"
        override val method: String
            get() = HttpMethod.PUT
        override fun handle(httpExchange: HttpExchange) {

        }
    }

    val getUserMethod = object: RequestHandlerMethod() {
        override val path: String
            get() = "/users/{id}"
        override fun handle(httpExchange: HttpExchange) {

        }
    }

    val getUserAccountMethod = object: RequestHandlerMethod() {
        override val path: String
            get() = "/users/{id}/account/{accountId}"
        override fun handle(httpExchange: HttpExchange) {

        }
    }

    val postUserAccountMethod = object: RequestHandlerMethod() {
        override val path: String
            get() = "/users/{id}/account/"
        override val method: String
            get() = HttpMethod.POST
        override fun handle(httpExchange: HttpExchange) {

        }
    }

    val deleteUserAccountMethod = object: RequestHandlerMethod() {
        override val path: String
            get() = "/users/{id}/account/"
        override val method: String
            get() = HttpMethod.DELETE
        override fun handle(httpExchange: HttpExchange) {

        }
    }

    val mockHttpMethodList = listOf(
        getNotificationMethod, getNotificationsMethod, postNotificationMethod, mainMethod, getNotificationsSseMethod,
        postUserMethod, putUserMethod, getUserMethod, getUserAccountMethod, postUserAccountMethod, deleteUserAccountMethod
    )
}
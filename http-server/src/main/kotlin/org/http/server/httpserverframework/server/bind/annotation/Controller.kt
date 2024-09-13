package org.http.server.httpserverframework.server.bind.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Controller(
    val path: String = ""
)
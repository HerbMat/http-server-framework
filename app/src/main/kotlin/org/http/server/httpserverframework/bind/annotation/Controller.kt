package org.http.server.httpserverframework.bind.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Controller(
    val path: String = ""
)
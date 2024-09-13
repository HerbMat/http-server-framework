package org.http.server.httpserverframework.reflection

import java.nio.file.Files
import java.nio.file.Path

object ReflectionUtils {
    fun loadContextClasses(): Sequence<String> {
        return System.getProperty("java.class.path")
            .split(System.getProperty("path.separator"))
            .asSequence()
            .map { Path.of("$it/META-INF/classIndex.inf") }
            .filter { Files.exists(it) }
            .map { Files.readAllLines(it) }
            .flatten()
    }

    fun loadClass(fullName: String): Class<*>? {
        return Class.forName(fullName.removeSuffix(".class"))
    }

    fun hasAnnotation(objectClass: Class<*>, annotation: Class<out Annotation>): Boolean {
        return objectClass.isAnnotationPresent(annotation)
    }
}
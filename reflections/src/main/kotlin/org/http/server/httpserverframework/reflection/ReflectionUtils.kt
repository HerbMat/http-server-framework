package org.http.server.httpserverframework.reflection

import java.io.BufferedReader
import java.io.InputStreamReader

object ReflectionUtils {
    fun loadClassNamesInPackage(packageName: String): Sequence<String> {
        return ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replace('.', '/'))
            ?.use { resourceStream -> BufferedReader(InputStreamReader(resourceStream)).use {
                    bf -> return@use bf.lines()
                .map { extractClasses(it, packageName) }
                .toList()
                .flatten()
                .asSequence()
            } } ?: sequenceOf()
    }

    fun extractClasses(name: String, packageName: String): List<String> {
        if (name.endsWith(".class")) {
            return listOf("$packageName.$name")
        }
        return loadClassNamesInPackage("$packageName.$name").toList()
    }

    fun loadClass(fullName: String): Class<*>? {
        return Class.forName(fullName.removeSuffix(".class"))
    }

    fun hasAnnotation(objectClass: Class<*>, annotation: Class<out Annotation>): Boolean {
        return objectClass.isAnnotationPresent(annotation)
    }
}
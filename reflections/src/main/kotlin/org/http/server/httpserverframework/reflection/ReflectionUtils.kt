package org.http.server.httpserverframework.reflection

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.stream.Collectors

object ReflectionUtils {
    fun loadClassNamesInPackage(packageName: String): Sequence<String> {
        return ClassLoader.getSystemResources(packageName.replace('.', '/'))
            .toList()
            .map { resourceStream ->
                BufferedReader(InputStreamReader(resourceStream.openStream())).use { bf ->
                    return@use bf.lines()
                        .map { extractClasses(it, packageName) }
                        .collect(Collectors.toList())
                        .toList()
                }.flatten()
            }.flatten()
            .asSequence()
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
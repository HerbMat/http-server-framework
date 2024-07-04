package org.http.server.httpserverframework.task.indexer

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.stream.Collectors

class ContextIndexer(private val buildPath: String, private val indexFile: String = "classIndex.inf") {
    fun createProjectIndex() {
        val listOfFiles = Files.walk(Path.of("$buildPath/classes/kotlin/main"))
            .filter(Files::isRegularFile)
            .filter{ path -> !path.fileName.toString().contains("$")}
            .filter{ path -> path.fileName.toString().endsWith(".class")}
            .collect(Collectors.toList())
        val classList = listOfFiles
            .map { filePath -> filePath.toString().removePrefix("$buildPath/classes/kotlin/main/") }
            .map { filePath -> filePath.replace('/', '.') }
            .map { filePath -> filePath.removeSuffix(".class") }
        val pathToIndexFile = Path.of("$buildPath/classes/kotlin/main/META-INF/$indexFile")
        if (Files.exists(pathToIndexFile)) {
            Files.delete(pathToIndexFile)
        }
        Files.createFile(pathToIndexFile)
        classList.forEach { classPath ->  Files.writeString(pathToIndexFile, "$classPath${System.lineSeparator()}", StandardOpenOption.APPEND) }
        println("OK")
    }
}
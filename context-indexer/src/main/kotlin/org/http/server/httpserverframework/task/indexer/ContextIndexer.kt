package org.http.server.httpserverframework.task.indexer

import org.http.server.httpserverframework.prototypes.Component
import org.objectweb.asm.*
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class ContextIndexer(private val buildPath: String, private val indexFile: String = "classIndex.inf") {

    fun createProjectIndex() {
        val listOfFiles = Files.walk(Path.of("$buildPath/classes/kotlin/main"))
            .filter(Files::isRegularFile)
            .filter{ path -> !path.fileName.toString().contains("$")}
            .filter{ path -> path.fileName.toString().endsWith(".class")}
            .filter { classPath ->  itContainsComponentAnnotation(classPath.toString())}
        val pathToIndexFile = Path.of("$buildPath/classes/kotlin/main/META-INF/$indexFile")
        if (Files.exists(pathToIndexFile)) {
            Files.delete(pathToIndexFile)
        }
        Files.createFile(pathToIndexFile)
        listOfFiles
            .map { filePath -> filePath.toString().removePrefix("$buildPath/classes/kotlin/main/") }
            .map { filePath -> filePath.replace('/', '.') }
            .map { filePath -> filePath.removeSuffix(".class") }
            .forEach { classPath ->  Files.writeString(pathToIndexFile, "$classPath${System.lineSeparator()}", StandardOpenOption.APPEND) }
        println("OK")
    }

    private fun itContainsComponentAnnotation(classPath: String): Boolean {
        println(classPath)

        val annotations =  BinaryClassReader.getClassAnnotations(classPath)
            .map { it.removePrefix("L") }
            .map { it.removeSuffix(";") }
            .map { it.replace('/', '.') }

        return annotations.any { it == Component::class::java.get().name }
    }

    private fun hasAnnotation(classFilePath: String, annotationClass: Class<out Annotation>): Boolean {
        val annotationDescriptor = Type.getDescriptor(annotationClass)
        FileInputStream(classFilePath).use { fis ->
            val classReader = ClassReader(fis)
            val visitor = AnnotationCheckerClassVisitor(annotationDescriptor)
            classReader.accept(visitor, 0)
            return visitor.hasAnnotation
        }
    }

    class AnnotationCheckerClassVisitor(
        private val annotationDescriptor: String
    ) : ClassVisitor(Opcodes.ASM9) {
        var hasAnnotation = false

        override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
            if (descriptor == annotationDescriptor) {
                hasAnnotation = true
            }
            return super.visitAnnotation(descriptor, visible)
        }
    }
}
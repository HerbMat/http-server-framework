package org.http.server.httpserverframework.bootstrap


import org.http.server.httpserverframework.reflection.ReflectionUtils
import java.lang.RuntimeException

class BootstrapLoader(path: String) {
    private val allClassNames = ReflectionUtils.loadClassNamesInPackage(path)

    fun prepareContext(mainClassAnnotation: Class<out Annotation>): ServerContext {
        val serverContext = ServerContext()
        getAllDeclaredClasses()
            .filter { ReflectionUtils.hasAnnotation(it, mainClassAnnotation) }
            .forEach { getObject(it, serverContext) }
        return serverContext
    }

    private fun getAllDeclaredClasses() = allClassNames
        .map { ReflectionUtils.loadClass(it) }
        .filterNotNull()

    private fun getObject(classType: Class<*>, serverContext: ServerContext): Any {
        return serverContext.loadedObjects.getOrPut(classType) { createObject(classType, serverContext) }
    }

    private fun createObject(classType: Class<*>, serverContext: ServerContext): Any {
        val constructors = classType.constructors
        if (constructors.isNotEmpty()) {
            val constructor = constructors[0]
            val parameters = constructor.parameterTypes
            val constructorParameters = parameters.map {
                it.cast(getObject(it, serverContext))
            }

            if (parameters.isEmpty()) {
                return constructor.newInstance()
            }
            return constructor.newInstance(*constructorParameters.toTypedArray())
        }
        if (classType.isInterface) {
            val newType = getAllDeclaredClasses()
                .firstOrNull { it.interfaces.contains(classType) }
            if (newType != null) {
                return getObject(newType, serverContext)
            }
        }
        throw RuntimeException("Error during context initialization.")
    }
}
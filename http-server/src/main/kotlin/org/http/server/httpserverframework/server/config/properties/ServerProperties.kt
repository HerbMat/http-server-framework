package org.http.server.httpserverframework.server.config.properties

import java.io.FileInputStream
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.util.*

class ServerProperties(private val serverPropertiesFileName: String) {

    constructor() : this(DEFAULT_SERVER_PROPERTIES_FILE_NAME)

    companion object {
        const val DEFAULT_SERVER_PROPERTIES_FILE_NAME = "server.properties"
        const val PORT_PROPERTY_NAME = "port"
        const val BACKLOG_PROPERTY_NAME = "backlog"
        const val HTTPS_ENABLED_PROPERTY_NAME = "https.enabled"
        const val CERT_FILE_PATH = "cert.file.path"
    }

    private val regex = Regex("[0-9]+")

    private val path = Thread.currentThread().contextClassLoader.getResource(serverPropertiesFileName)?.path ?: ""
    private val properties = Properties().also { it.load(FileInputStream(path)) }

    val port = getPropertyAsInt(PORT_PROPERTY_NAME)

    val backLog = getPropertyAsInt(BACKLOG_PROPERTY_NAME)
    val httpsEnabled = getPropertyAsBoolean(HTTPS_ENABLED_PROPERTY_NAME)
    val certFileName = getProperty(CERT_FILE_PATH)

    private fun getPropertyAsInt(propertyName: String): Int {
        val property = getProperty(propertyName)
        if (regex.matches(property)) {
            return property.toInt()
        }
        throw IllegalArgumentException("Bad format $propertyName should have numeric value")
    }

    private fun getPropertyAsBoolean(propertyName: String): Boolean {
        val property = getProperty(propertyName)
        return property.toBoolean()
//        return when (property.lowercase()) {
//            "true" -> true
//            "false" -> false
//            else -> throw IllegalArgumentException("Bad format $propertyName should be true or false value")
//        }
    }

    private fun getProperty(propertyName: String): String {
        if (!properties.containsKey(propertyName)) {
            throw RuntimeException("Missing property $propertyName")
        }
        return properties.getProperty(propertyName)
    }
}
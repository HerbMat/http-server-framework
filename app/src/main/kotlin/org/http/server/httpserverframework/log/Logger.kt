package org.http.server.httpserverframework.log

import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.Logger as UtilLogger

class Logger(name: String) {
    private val logger = UtilLogger.getLogger(name)

    companion object {
        fun getLogger(name: String): Logger {
            return Logger(name)
        }
        private const val DEFAULT_LOGGER_PROPERTIES_FILE_NAME = "logger.properties"
        init {
            val pathStream = Thread.currentThread().contextClassLoader.getResourceAsStream(DEFAULT_LOGGER_PROPERTIES_FILE_NAME)
            LogManager.getLogManager().readConfiguration(pathStream)
            println("Dun")
        }
    }

    fun isDebugEnabled(): Boolean = logger.isLoggable(Level.FINE)
    fun isInfoEnabled(): Boolean = logger.isLoggable(Level.INFO)
    fun isErrorEnabled(): Boolean = logger.isLoggable(Level.SEVERE)

    fun debug(message: String) {
        logger.fine(message)
    }

    fun info(message: String) {
        logger.finest(message)
    }

    fun error(message: String) {
        logger.severe(message)
    }
}
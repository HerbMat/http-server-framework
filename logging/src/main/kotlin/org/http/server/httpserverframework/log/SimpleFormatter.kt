package org.http.server.httpserverframework.log

import java.io.PrintWriter
import java.io.StringWriter
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.logging.Formatter
import java.util.logging.LogManager
import java.util.logging.LogRecord

class SimpleFormatter: Formatter() {
    companion object {
        private const val DEFAULT_FORMAT = "%1\$tb %1\$td, %1\$tY %1\$tl:%1\$tM:%1\$tS %1\$Tp %2\$s%n%3\$s: %4\$s %5\$s%n"
        private const val SIMPLE_FORMAT_PROPERTY = "java.util.logging.SimpleFormatter.format"
    }

    private val format = LogManager.getLogManager().getProperty(SIMPLE_FORMAT_PROPERTY) ?: DEFAULT_FORMAT

    override fun format(logRecord: LogRecord): String {
        val zdt = ZonedDateTime.ofInstant(logRecord.instant, ZoneId.systemDefault())
        val message = this.formatMessage(logRecord)
        val throwable = logRecord.thrown?.let {
            StringWriter().use sw@ { sw ->
                PrintWriter(sw).use { pw ->
                    pw.println()
                    logRecord.thrown.printStackTrace(pw)
                    return@sw sw.toString()
                }
            }
        }
        return String.format(this.format, zdt, logRecord.loggerName, logRecord.level.localizedName, message, throwable)
    }
}
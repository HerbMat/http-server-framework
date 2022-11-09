package org.http.server.httpserverframework

import com.sun.net.httpserver.HttpServer
import org.http.server.httpserverframework.config.properties.ServerProperties
import org.http.server.httpserverframework.handlers.RequestHandler
import org.http.server.httpserverframework.log.Logger
import java.net.InetSocketAddress

class ServerApp(private val serverProperties: ServerProperties) {
    companion object {
        private val log = Logger.getLogger(this::class.toString())

        fun create(requestHandler: RequestHandler): ServerApp {
            val serverProperties = ServerProperties()
            log.info("Creating the server app on port ${serverProperties.port}")
            val serverApp = ServerApp(serverProperties)
            serverApp.init(requestHandler)

            return serverApp
        }
    }

    fun start() {
        log.info("Starting the server on port ${serverProperties.port}")
        server.start()
        log.info("Server started on port ${serverProperties.port}")
    }

    private val server = HttpServer.create(InetSocketAddress(serverProperties.port), serverProperties.backLog)

    private fun init(requestHandler: RequestHandler) {
        log.debug("Attaching request handler to server")
        server.createContext(RequestHandler.PATH, requestHandler)
        server.executor = null
    }
}
package org.http.server.httpserverframework.server

import com.sun.net.httpserver.HttpServer
import com.sun.net.httpserver.HttpsConfigurator
import com.sun.net.httpserver.HttpsParameters
import com.sun.net.httpserver.HttpsServer
import org.http.server.httpserverframework.log.Logger
import org.http.server.httpserverframework.server.config.properties.ServerProperties
import org.http.server.httpserverframework.server.handlers.RequestHandler
import java.io.FileNotFoundException
import java.net.InetSocketAddress
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

class ServerApp(private val serverProperties: ServerProperties) {
    companion object {
        const val ENCRYPTION_PROTOCOL = "TLS"
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

    private val server: HttpServer = if (serverProperties.httpsEnabled) {
        val httpsServer = HttpsServer.create(InetSocketAddress(serverProperties.port), serverProperties.backLog)
        httpsServer.httpsConfigurator = object : HttpsConfigurator(initializeSSL()) {
            override fun configure(params: HttpsParameters) {
                val context = sslContext
                val sslEngine = context.createSSLEngine()
                params.needClientAuth = false
                params.cipherSuites = sslEngine.enabledCipherSuites
                params.protocols = sslEngine.enabledProtocols
                params.setSSLParameters(context.supportedSSLParameters)
            }
        }
        httpsServer
    } else {
        HttpServer.create(InetSocketAddress(serverProperties.port), serverProperties.backLog)
    }

    private fun init(requestHandler: RequestHandler) {
        log.debug("Attaching request handler to server")
        server.createContext(RequestHandler.PATH, requestHandler)
        server.executor = null
    }

    private fun initializeSSL(): SSLContext {
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        val password = "framework".toCharArray()
        val keyManager = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        val fileInput = ClassLoader.getSystemClassLoader().getResourceAsStream(serverProperties.certFileName)
            ?: throw FileNotFoundException("Not found cert file.")
        fileInput.use {
            keyStore.load(fileInput, password)
            keyManager.init(keyStore, password)
            trustManagerFactory.init(keyStore)
        }
        val sslContext = SSLContext.getInstance(ENCRYPTION_PROTOCOL)
        sslContext.init(keyManager.keyManagers, trustManagerFactory.trustManagers, null)
        return sslContext
    }
}
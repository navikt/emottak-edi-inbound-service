package no.nav.emottak.edi.inbound.plugin

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import org.slf4j.event.Level

fun Application.configureCallLogging() {
    val log = KotlinLogging.logger {}

    intercept(ApplicationCallPipeline.Monitoring) {
        val start = System.currentTimeMillis()

        proceed()

        if (!call.request.path().startsWith("/api")) return@intercept

        val duration = System.currentTimeMillis() - start
        val status = call.response.status() ?: HttpStatusCode.NotFound
        val method = call.request.httpMethod.value
        val path = call.request.path()
        val userAgent = call.request.headers["User-Agent"] ?: "Unknown"

        val message = """
            Status: $status
            Method: $method
            Path: $path
            User-Agent: $userAgent
            Duration: ${duration}ms
        """.trimIndent()

        val level = when (status.value) {
            in 500..599 -> Level.ERROR
            404 -> Level.WARN
            else -> Level.INFO
        }

        when (level) {
            Level.ERROR -> log.error { message }
            Level.WARN -> log.warn { message }
            Level.INFO -> log.info { message }
            else -> log.debug { message }
        }
    }
}

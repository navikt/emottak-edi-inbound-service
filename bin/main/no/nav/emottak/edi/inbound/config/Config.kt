package no.nav.emottak.edi.inboundservice.config

import io.ktor.client.plugins.logging.LogLevel
import no.nav.emottak.utils.config.Server

data class Config(
    val server: Server,
    val httpClient: HttpClient,
    val httpTokenClient: HttpTokenClient
)

@JvmInline
value class Timeout(val value: Long)

data class HttpClient(
    val connectionTimeout: Timeout,
    val acceptTypeHeader: AcceptTypeHeader,
    val logLevel: LogLevel
) {
    @JvmInline
    value class AcceptTypeHeader(val value: String)
}

data class HttpTokenClient(val connectionTimeout: Timeout)

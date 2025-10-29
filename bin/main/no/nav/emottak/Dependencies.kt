package no.nav.emottak

import arrow.fx.coroutines.ExitCase
import arrow.fx.coroutines.ResourceScope
import arrow.fx.coroutines.await.awaitAll
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders.Accept
import io.ktor.serialization.kotlinx.json.json
import io.micrometer.prometheus.PrometheusConfig.DEFAULT
import io.micrometer.prometheus.PrometheusMeterRegistry
import no.nav.emottak.edi.inbound.config.Config

private val log = KotlinLogging.logger {}

data class Dependencies(
    val httpClient: HttpClient,
    val prometheusMeterRegistry: PrometheusMeterRegistry
)

suspend fun ResourceScope.dependencies(): Dependencies = awaitAll {
    val config = config()

    val metricsRegistry = async { metricsRegistry() }
    val httpClientEngine = async { httpClientEngine() }.await()
    val httpClient = async { httpClient(config, httpClientEngine) }

    Dependencies(
        httpClient.await(),
        metricsRegistry.await()
    )
}

internal suspend fun ResourceScope.metricsRegistry(): PrometheusMeterRegistry =
    install({ PrometheusMeterRegistry(DEFAULT) }) { p, _: ExitCase ->
        p.close().also { log.info { "Closed prometheus registry" } }
    }

internal suspend fun ResourceScope.httpClientEngine(): HttpClientEngine =
    install({ CIO.create() }) { e, _: ExitCase -> e.close().also { log.info { "Closed http client engine" } } }

private fun httpClient(
    config: Config,
    clientEngine: HttpClientEngine
): HttpClient = HttpClient(clientEngine) {
    install(HttpTimeout) {
        connectTimeoutMillis = config.httpClient.connectionTimeout.value
    }
    install(Logging) { level = config.httpClient.logLevel }
    install(ContentNegotiation) { json() }

    defaultRequest {
        val httpClient = config.httpClient
        header(Accept, httpClient.acceptTypeHeader.value)
    }
}

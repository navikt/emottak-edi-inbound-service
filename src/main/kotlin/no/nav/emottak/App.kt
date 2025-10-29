package no.nav.emottak

import arrow.continuations.SuspendApp
import arrow.continuations.ktor.server
import arrow.core.raise.result
import arrow.fx.coroutines.resourceScope
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.Application
import io.ktor.server.netty.Netty
import io.micrometer.prometheus.PrometheusMeterRegistry
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.awaitCancellation
import no.nav.emottak.edi.inbound.plugin.configureCallLogging
import no.nav.emottak.edi.inbound.plugin.configureContentNegotiation
import no.nav.emottak.edi.inbound.plugin.configureMetrics
import no.nav.emottak.edi.inbound.plugin.configureRoutes

private val log = KotlinLogging.logger {}

fun main() = SuspendApp {
    result {
        resourceScope {
            val deps = dependencies()

            server(
                Netty,
                port = config().server.port.value,
                preWait = config().server.preWait,
                module = ediInboundService(deps.prometheusMeterRegistry)
            )

            awaitCancellation()
        }
    }
        .onFailure { error -> if (error !is CancellationException) logError(error) }
}

internal fun ediInboundService(
    prometheusMeterRegistry: PrometheusMeterRegistry
): Application.() -> Unit {
    return {
        configureMetrics(meterRegistry = prometheusMeterRegistry)
        configureContentNegotiation()
        configureRoutes(prometheusMeterRegistry)
        configureCallLogging()
    }
}

private fun logError(t: Throwable) =
    log.error { "Shutdown emottak-edi-inbound-service due to: ${t.stackTraceToString()}" }

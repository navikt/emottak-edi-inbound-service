package no.nav.emottak.edi.inbound.plugin

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.micrometer.core.instrument.MeterRegistry

fun Application.configureMetrics(meterRegistry: MeterRegistry) {
    install(MicrometerMetrics) {
        registry = meterRegistry
    }
}

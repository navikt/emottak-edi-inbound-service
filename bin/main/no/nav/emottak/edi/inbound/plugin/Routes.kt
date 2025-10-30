package no.nav.emottak.edi.inbound.plugin

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import io.micrometer.prometheus.PrometheusMeterRegistry
import no.nav.emottak.edi.inbound.routes.healthRoutes

fun Application.configureRoutes(prometheusMeterRegistry: PrometheusMeterRegistry) {
    routing {
        healthRoutes(prometheusMeterRegistry)
    }
}

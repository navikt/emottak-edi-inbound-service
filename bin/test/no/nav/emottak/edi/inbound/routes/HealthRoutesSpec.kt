package no.nav.emottak.edi.inbound.routes

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry

class HealthRoutesSpec : StringSpec({
    "GET /prometheus returns OK and has incremented a test_counter" {
        val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
        registry.counter("test_counter", "test", "value").increment()

        testApplication {
            application {
                routing { healthRoutes(registry) }
            }
            val response = client.get("/prometheus")
            response.status shouldBe OK
            val body = response.bodyAsText()
            body shouldNotBe ""
            body shouldContain "test_counter"
        }
    }

    "GET /internal/health/liveness returns alive message" {
        val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
        testApplication {
            application {
                routing { healthRoutes(registry) }
            }
            val response = client.get("/internal/health/liveness")
            response.status shouldBe OK
            response.bodyAsText() shouldBe "I'm alive! :)"
        }
    }

    "GET /internal/health/readiness returns ready message" {
        val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
        testApplication {
            application {
                routing { healthRoutes(registry) }
            }
            val response = client.get("/internal/health/readiness")
            response.status shouldBe OK
            response.bodyAsText() shouldBe "I'm ready! :)"
        }
    }
})


dependencyResolutionManagement {

    versionCatalogs {
        create("libs") {
            version("arrow", "2.0.1")
            version("jwt", "4.4.0")
            version("nimbus-jwt", "9.31")
            version("suspendapp", "0.5.0")
            version("ktor", "3.3.1")
            version("kotlin-logging", "7.0.3")
            version("token-validation-ktor", "5.0.30")
            version("eclipse-angus", "2.0.2")
            version("hoplite", "2.8.2")
            version("prometheus", "1.12.4")
            version("logback", "1.5.13")
            version("logstash", "7.4")
            version("janino", "3.1.12")
            version("emottak-utils", "0.3.3")

            library("arrow-core", "io.arrow-kt", "arrow-core").versionRef("arrow")
            library("arrow-functions", "io.arrow-kt", "arrow-functions").versionRef("arrow")
            library("arrow-fx-coroutines", "io.arrow-kt", "arrow-fx-coroutines").versionRef("arrow")
            library("arrow-resilience", "io.arrow-kt", "arrow-resilience").versionRef("arrow")
            library("arrow-suspendapp", "io.arrow-kt", "suspendapp").versionRef("suspendapp")
            library("arrow-suspendapp-ktor", "io.arrow-kt", "suspendapp-ktor").versionRef("suspendapp")

            library("hoplite-core", "com.sksamuel.hoplite", "hoplite-core").versionRef("hoplite")
            library("hoplite-hocon", "com.sksamuel.hoplite", "hoplite-hocon").versionRef("hoplite")

            library("jwt", "com.auth0", "java-jwt").versionRef("jwt")
            library("nimbus-jwt", "com.nimbusds", "nimbus-jose-jwt").versionRef("nimbus-jwt")

            library("ktor-server-core", "io.ktor", "ktor-server-core").versionRef("ktor")
            library("ktor-server-netty", "io.ktor", "ktor-server-netty").versionRef("ktor")
            library("ktor-server-call-logging-jvm", "io.ktor", "ktor-server-call-logging-jvm").versionRef("ktor")
            library("ktor-server-content-negotiation", "io.ktor", "ktor-server-content-negotiation").versionRef("ktor")
            library("ktor-client-content-negotiation", "io.ktor", "ktor-client-content-negotiation").versionRef("ktor")
            library("ktor-serialization-kotlinx-json", "io.ktor", "ktor-serialization-kotlinx-json").versionRef("ktor")
            library("ktor-client-core", "io.ktor", "ktor-client-core").versionRef("ktor")
            library("ktor-client-cio", "io.ktor", "ktor-client-cio").versionRef("ktor")
            library("ktor-client-auth", "io.ktor", "ktor-client-auth").versionRef("ktor")
            library("ktor-client-logging", "io.ktor", "ktor-client-logging").versionRef("ktor")

            library("ktor-server-metrics-micrometer", "io.ktor", "ktor-server-metrics-micrometer").versionRef("ktor")
            library("micrometer-registry-prometheus", "io.micrometer", "micrometer-registry-prometheus").versionRef("prometheus")

            library("kotlin-logging", "io.github.oshai", "kotlin-logging-jvm").versionRef("kotlin-logging")
            library("logback-classic", "ch.qos.logback", "logback-classic").versionRef("logback")
            library("logback-logstash", "net.logstash.logback", "logstash-logback-encoder").versionRef("logstash")
            library("janino", "org.codehaus.janino", "janino").versionRef("janino")

            library("ktor-server-auth-jvm", "io.ktor", "ktor-server-auth-jvm").versionRef("ktor")
            library("token-validation-ktor-v3", "no.nav.security", "token-validation-ktor-v3").versionRef("token-validation-ktor")

            library("emottak-utils", "no.nav.emottak", "emottak-utils").versionRef("emottak-utils")

            bundle("prometheus", listOf("ktor-server-metrics-micrometer", "micrometer-registry-prometheus"))
            bundle("logging", listOf("logback-classic", "logback-logstash"))
        }

        create("testLibs") {
            version("arrow", "2.0.0")
            version("ktor", "3.3.1")
            version("ktor-server-test", "3.0.3")
            version("kotest", "5.9.1")
            version("mock-oauth2", "2.1.2")
            version("testcontainers", "1.18.1")
            version("kotest-extensions", "2.0.2")
            version("turbine", "1.2.0")

            library("ktor-server-test-host", "io.ktor", "ktor-server-test-host").versionRef("ktor-server-test")
            library("ktor-client-mock", "io.ktor", "ktor-client-mock").versionRef("ktor")
            library("mock-oauth2-server", "no.nav.security", "mock-oauth2-server").versionRef("mock-oauth2")

            library("kotest-runner-junit5", "io.kotest", "kotest-runner-junit5").versionRef("kotest")
            library("kotest-framework-datatest", "io.kotest", "kotest-framework-datatest").versionRef("kotest")
            library("kotest-extensions-jvm", "io.kotest", "kotest-extensions-jvm").versionRef("kotest")

            library("kotest-extensions-testcontainers", "io.kotest.extensions", "kotest-extensions-testcontainers").versionRef("kotest-extensions")

            library("kotest-assertions-arrow", "io.kotest.extensions", "kotest-assertions-arrow").versionRef("arrow")

            library("testcontainers", "org.testcontainers", "testcontainers").versionRef("testcontainers")
            library("testcontainers-postgresql", "org.testcontainers", "postgresql").versionRef("testcontainers")

            library("turbine", "app.cash.turbine", "turbine").versionRef("turbine")

            bundle("kotest", listOf("kotest-runner-junit5", "kotest-framework-datatest"))
        }
    }

    repositories {
        mavenCentral()
        maven {
            name = "Nav token-support"
            url = uri("https://maven.pkg.github.com/navikt/token-support")
            credentials {
                username = "token"
                password = System.getenv("GITHUB_TOKEN")
            }
        }
        maven {
            name = "Mock OAUTH2 server"
            url = uri("https://maven.pkg.github.com/navikt/mock-oauth2-server")
            credentials {
                username = "token"
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

rootProject.name = "emottak-edi-inbound-service"

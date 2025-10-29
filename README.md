# emottak-edi-inbound-service

Only `emottak-edi-adapter` will call this app (`emottak-edi-inbound-service`).

---

## API Endpoints

### Health and metrics

| Path                         | Description                 |
|------------------------------|-----------------------------|
| `/internal/health/liveness`  | Returns “I'm alive! :)”     |
| `/internal/health/readiness` | Returns “I'm ready! :)”     |
| `/prometheus`                | Prometheus metrics endpoint |

----

## Development

### Running Tests in IntelliJ

To run the Spec (Kotest) tests directly in IntelliJ IDEA, you must install the **Kotest** plugin:

- Open IntelliJ IDEA and go to **Settings -> Plugins**.
- Search for "Kotest" and install the plugin.
- Restart IntelliJ if prompted.

With the Kotest plugin installed, you can run and debug tests from the IDE as usual (right-click test classes or methods, or use the gutter icons). Without the plugin, tests may not be detected or runnable in IntelliJ, and you will need to run them via Gradle instead.

---

## Technical Choices

### Logging with KotlinLogging

This project uses [KotlinLogging](https://github.com/oshai/kotlin-logging) instead of SLF4J directly.

**Benefits:**

- **No manual class names**: `KotlinLogging.logger {}` automatically infers the class name instead of requiring `LoggerFactory.getLogger(ClassName::class.java)`.
- **Lazy evaluation**: The lambda format means log messages are only evaluated when that log level is enabled.

**Why we use the lambda format:**

```kotlin
// BAD - string is built even if INFO is disabled
log.info("Processing message: $messageId")

// OK - lazy evaluation with placeholders
log.info("Processing message: {}", messageId)

// BEST - lazy evaluation with Kotlin string templates  
log.info { "Processing message: $messageId" }
```

The lambda format gives you **lazy evaluation** (**performance benefit**) while keeping Kotlin's string templates (readability benefit). 
The placeholder style achieves the same performance, but the lambda format is more readable and natural in Kotlin.

**Example:**

```kotlin
private val log = KotlinLogging.logger {}

// This expensive operation only runs if DEBUG is enabled
log.debug { "Result: ${expensiveOperation()}" }
```

### Logback Configuration & Local Logging

This project uses [Logback](https://logback.qos.ch/) as the logging backend. By default, logs are output in JSON format for production and cloud environments.

For local development, you can enable _human-readable_, plain text logs by setting the environment variable `LOCAL_LOGGING` to `true` before starting the application:

```bash
export LOCAL_LOGGING=true
```

When `LOCAL_LOGGING` is set, Logback switches to a simple console pattern for easier reading and debugging. This flow is controlled in `src/main/resources/logback.xml`.

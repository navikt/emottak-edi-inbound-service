package no.nav.emottak

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import io.ktor.server.application.ApplicationCall

private const val MESSAGE = "message"

fun Raise<ValidationError>.messageId(call: ApplicationCall): String =
    call.parameters[MESSAGE]!!.also {
        ensure(it.isNotBlank()) { MessageNotFound }
    }

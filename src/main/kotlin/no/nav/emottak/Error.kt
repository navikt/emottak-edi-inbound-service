package no.nav.emottak

import io.ktor.http.ContentType.Text.Plain
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.content.TextContent

sealed interface MessageError
sealed interface ValidationError : MessageError

data object MessageNotFound : ValidationError

fun MessageError.toContent(): TextContent =
    when (this) {
        is MessageNotFound ->
            TextContent("Message was not found")
    }

private fun TextContent(
    content: String,
    statusCode: HttpStatusCode = BadRequest
): TextContent = TextContent(content, Plain, statusCode)

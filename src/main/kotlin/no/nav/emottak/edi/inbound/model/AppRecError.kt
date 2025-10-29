package no.nav.emottak.edi.inbound.model

import kotlinx.serialization.Serializable

@Serializable
data class AppRecError(
    val errorCode: String,
    val details: String,
    val description: String,
    val oid: String
)

package com.closedcircuit.closedcircuitapplication.core.network.dto

import com.closedcircuit.closedcircuitapplication.core.network.EnvelopeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule

@Serializable(with = EnvelopeSerializer::class)
class Envelope<T>(
    val message: String? = null,
    val data: T? = null,
    val errors: Map<String, Any?>? = null
)

val envelopeModule = SerializersModule {
//    contextual(EnvelopeSerializer(KSerializer()))
}
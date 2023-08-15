package com.closedcircuit.closedcircuitapplication.core.network

import com.closedcircuit.closedcircuitapplication.core.network.dto.Envelope
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

internal suspend fun <T> HttpResponse.toCommonResponse(dataSerializer: KSerializer<T>): Response<Envelope<T>> {

    val json = Json { ignoreUnknownKeys = true }
    val envelope = json.decodeFromString(
        EnvelopeSerializer(dataSerializer),
        this.bodyAsText()
    )

    return object : Response<Envelope<T>> {
        override val isSuccessful: Boolean
            get() = this@toCommonResponse.status.isSuccess()

        override val code: Int
            get() = this@toCommonResponse.status.value

        override val description: String
            get() = this@toCommonResponse.status.description

        override fun body(): Envelope<T> {
            return envelope
        }

        override suspend fun bodyAsString(): String {
            return this@toCommonResponse.bodyAsText()
        }

        override fun headers(): Set<Map.Entry<String, List<String>>> {
            return this@toCommonResponse.headers.entries()
        }
    }
}
package com.closedcircuit.closedcircuitapplication.core.network

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.SuspendResponseConverter
import de.jensklingenberg.ktorfit.internal.TypeData
import io.github.aakira.napier.Napier
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.util.reflect.TypeInfo
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.serializer

class DefaultResponseConverter : SuspendResponseConverter {

    override fun supportedType(typeData: TypeData, isSuspend: Boolean): Boolean {
        return if (isSuspend) {
            typeData.qualifiedName == "com.closedcircuit.closedcircuitapplication.core.network.ApiResponse"
        } else {
            typeData.qualifiedName == "kotlinx.coroutines.flow.Flow"
        }
    }

    @OptIn(InternalSerializationApi::class)
    override suspend fun <RequestType> wrapSuspendResponse(
        typeData: TypeData,
        requestFunction: suspend () -> Pair<TypeInfo, HttpResponse>,
        ktorfit: Ktorfit
    ): Any {
        return try {
            val (info, response) = requestFunction()
            if (response.status.isSuccess()) {
                val dataSerializer = typeData.typeArgs.first().typeInfo.type.serializer()
                val json = Json { ignoreUnknownKeys = true }
                val envelope = json.decodeFromString(
                    EnvelopeSerializer(dataSerializer),
                    response.bodyAsText()
                )
                ApiResponse.create(envelope)
            } else {
                val jsonObject: JsonObject = Json.decodeFromString(response.bodyAsText())

                Napier.d("JSON:: $jsonObject")
                ApiResponse.create(Throwable("Something went wrong"))
            }
        } catch (e: Throwable) {
            ApiResponse.create(e)
        }
    }
}

class DefaultConverterFactory : Converter.Factory {


    @OptIn(InternalSerializationApi::class)
    override fun suspendResponseConverter(
        typeData: TypeData,
        ktorfit: Ktorfit
    ): Converter.SuspendResponseConverter<HttpResponse, *>? {
        return if (typeData.typeInfo.type == ApiResponse::class) {
            object : Converter.SuspendResponseConverter<HttpResponse, Any> {
                override suspend fun convert(response: HttpResponse): Any {
                    return if (response.status.isSuccess()) {
                        val dataSerializer = typeData.typeArgs.first().typeInfo.type.serializer()
                        val json = Json { ignoreUnknownKeys = true }
                        val envelope = json.decodeFromString(
                            EnvelopeSerializer(dataSerializer),
                            response.bodyAsText()
                        )
                        ApiResponse.create(envelope)
                    } else {
                        ApiResponse.create(Throwable("Something went wrong"))
                    }
                }
            }
        } else {
            null
        }
    }
}

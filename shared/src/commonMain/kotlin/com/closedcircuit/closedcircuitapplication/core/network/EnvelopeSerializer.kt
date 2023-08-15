package com.closedcircuit.closedcircuitapplication.core.network

import com.closedcircuit.closedcircuitapplication.core.network.dto.Envelope
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.nullable
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure


class EnvelopeSerializer<T>(private val dataSerializer: KSerializer<T>) :
    KSerializer<Envelope<T>> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("ApiResponseDataSerializer") {
            val dataDescriptor = dataSerializer.descriptor
            element("message", String.serializer().descriptor.nullable)
            element("data", dataDescriptor)
            element(
                "errors",
                MapSerializer(String.serializer(), String.serializer()).descriptor.nullable
            )
        }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): Envelope<T> {
        return decoder.decodeStructure(descriptor) {
            var data: T? = null
            var message: String? = null
            var errors: Map<String, String>? = null
            loop@ while (true) {
                when (val i = decodeElementIndex(descriptor)) {
                    0 -> message = decodeStringElement(descriptor, i)
                    1 -> data = decodeNullableSerializableElement(descriptor, i, dataSerializer)
                    2 -> errors = decodeNullableSerializableElement(
                        descriptor,
                        i,
                        MapSerializer(String.serializer(), String.serializer())
                    )

                    CompositeDecoder.DECODE_DONE -> break
                    else -> throw SerializationException("Unknown index $i")
                }
            }
            Envelope(data = data, message = message, errors = errors)
        }
    }


    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: Envelope<T>) {
        encoder.encodeStructure(descriptor) {
            encodeNullableSerializableElement(descriptor, 0, dataSerializer, value.data)
//            encodeNullableSerializableElement(descriptor, 1, ApiError.serializer(), value.error)
        }
    }
}
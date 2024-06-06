package elementix.trpc.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import kotlinx.serialization.serializer

@Serializer(forClass = Result::class)
class ResultSerializer<T>(private val tSerializer: KSerializer<T>): KSerializer<Result<T>> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ResultSerializer", tSerializer.descriptor) {
        element<Boolean>("isSuccess")
        element("value", tSerializer.descriptor)
    }

    override fun deserialize(decoder: Decoder): Result<T> = decoder.decodeStructure(descriptor) {
        var value: T? = null
        var throwable: Throwable? = null
        var isSuccess = false

        while (true) {
            val index = decodeElementIndex(descriptor)
            when {
                index == 0 -> isSuccess = decodeBooleanElement(descriptor, 1)
                index == 1 && isSuccess -> value = decodeSerializableElement(descriptor, 0, tSerializer)
                index == 1 && !isSuccess -> throwable = decodeSerializableElement(descriptor, 0, ThrowableSerializer)
                index == CompositeDecoder.DECODE_DONE -> break
                else -> error("Unexpected index: $index")
            }
        }

        if(isSuccess) {
            Result.success(value!!)
        } else {
            Result.failure(throwable!!)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: Result<T>) = encoder.encodeStructure(descriptor) {
        encodeBooleanElement(descriptor, 0, value.isSuccess)
        if (value.isSuccess) {
            encodeNullableSerializableElement(descriptor, 1, tSerializer, value.getOrNull())
        } else {
            val throwable = value.exceptionOrNull()!!
            encodeSerializableElement(descriptor, 1, ThrowableSerializer, throwable)
        }
    }
}
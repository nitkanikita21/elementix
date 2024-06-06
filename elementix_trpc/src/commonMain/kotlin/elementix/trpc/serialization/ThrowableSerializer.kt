package elementix.trpc.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*

@Serializer(forClass = Throwable::class)
object ThrowableSerializer: KSerializer<Throwable> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Throwable") {
        element<String>("message", isOptional = true)
    }

    override fun deserialize(decoder: Decoder): Throwable = decoder.decodeStructure(descriptor) {
        var msg: String? = null

        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> msg = decodeStringElement(descriptor, index)
                CompositeDecoder.DECODE_DONE -> break
                else -> error("Unexpected index: $index")
            }
        }

        Throwable(msg)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: Throwable) = encoder.encodeStructure(descriptor) {
        encodeNullableSerializableElement(descriptor, 0, String.serializer(), value.message)
    }
}
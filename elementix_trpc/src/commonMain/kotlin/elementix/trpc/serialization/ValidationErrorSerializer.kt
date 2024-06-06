package elementix.trpc.serialization

import io.konform.validation.ValidationError
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*

/*
object ValidationErrorSerializer : KSerializer<ValidationError> {
    class Serialized(override val dataPath: String, override val message: String) : ValidationError

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Result") {
        element<String>("dataPath")
        element<String>("message")
    }


    override fun deserialize(decoder: Decoder): ValidationError = decoder.decodeStructure(descriptor) {
        var dataPath: String = ""
        var message: String = ""
        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> dataPath = decodeStringElement(descriptor, 0)
                1 -> message = decodeStringElement(descriptor, 1)
                CompositeDecoder.DECODE_DONE -> break
                else -> error("Unexpected index: $index")
            }
        }
        Serialized(dataPath, message)
    }

    override fun serialize(encoder: Encoder, value: ValidationError) = encoder.encodeStructure(descriptor) {
        encodeStringElement(descriptor, 0, value.dataPath)
        encodeStringElement(descriptor, 1, value.message)
    }
}*/

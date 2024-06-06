package elementix.trpc.route

import io.konform.validation.ValidationError
import kotlinx.serialization.Serializable

@Serializable
data class SerializableError(
    private val msg: String
): Throwable(msg)
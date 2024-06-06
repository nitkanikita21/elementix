package elementix.trpc.route

import io.konform.validation.ValidationError
import kotlinx.serialization.Serializable

@Serializable
data class SerializableValidationError(
    val errors: List<ValidationError>
): Throwable(errors.map { it.message }.joinToString("; "))
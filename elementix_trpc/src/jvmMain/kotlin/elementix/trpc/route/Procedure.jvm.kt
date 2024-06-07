package elementix.trpc.route

import io.konform.validation.Invalid
import io.konform.validation.Valid
import io.konform.validation.Validation
import io.ktor.util.reflect.*
import kotlinx.coroutines.*
import kotlin.properties.Delegates
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf


actual class Procedure<I: Any?, R: Any?>() {
    actual var route: Route by Delegates.notNull()
    actual var name: String by Delegates.notNull()
    actual var validator: Validation<I>? = null

    private var handler: suspend (I) -> R by Delegates.notNull()
    var inputTypeInfo: TypeInfo by Delegates.notNull()
    var returnTypeInfo: TypeInfo by Delegates.notNull()


    fun bindHandler(handler : suspend (I) -> R) {
        this.handler = handler
    }

    suspend fun getResponse(input: I): Result<Deferred<R>> {
        val validation = validator?.validate(input)

        val result: Deferred<R> by lazy {
            CoroutineScope(Dispatchers.Default).async {
                handler(input)
            }
        }

        return when(validation) {
            is Valid -> Result.success(result)
            is Invalid -> Result.failure(SerializableValidationError(validation.errors))
            null -> Result.success(result)
        }
    }
}

actual inline fun <reified I: Any?, reified R: Any?> Procedure(
    route: Route,
    name: String,
    validator: Validation<I>?
): Procedure<I, R> {
    return Procedure<I, R>().apply {
        this.name = name
        this.validator = validator
        this.route = route
        this.inputTypeInfo = typeInfo<I>()
        this.returnTypeInfo = typeInfo<R>()

    }
}
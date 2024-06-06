package elementix.trpc.builder

import elementix.trpc.route.Procedure
import elementix.trpc.route.Route
import elementix.trpc.utils.toKebabCase
import io.konform.validation.Validation

inline fun <reified I: Any, reified T: Any> Route.procedure(): GetDelegate<Procedure<I, T>> {
    val procedure: Once<Procedure<I, T>> = Once()

    return GetDelegate { _, property ->
        procedure.getOrInit {
            Procedure<I, T>(
                this, property.name.toKebabCase(), null
            ).also { this@procedure.procedures[it.name] = it as Procedure<in Any, out Any>}
        }
    }
}

inline fun <reified I: Any, reified T: Any> Route.procedure(name: String): GetDelegate<Procedure<I, T>> {
    val procedure: Once<Procedure<I, T>> = Once()

    return GetDelegate { _, _ ->
        procedure.getOrInit {
            Procedure<I, T>(
                this, name, null
            ).also { this@procedure.procedures[it.name] = it as Procedure<in Any, out Any> }
        }
    }
}

inline fun <reified I: Any, reified T: Any> Route.procedure(validator: Validation<I>): GetDelegate<Procedure<I, T>> {
    val procedure: Once<Procedure<I, T>> = Once()

    return GetDelegate { _, property ->
        procedure.getOrInit {
            Procedure<I, T>(
                this, property.name.toKebabCase(), validator
            ).also { this@procedure.procedures[it.name] = it as Procedure<in Any, out Any> }
        }
    }
}

inline fun <reified I: Any, reified T: Any> Route.procedure(name: String, validator: Validation<I>): GetDelegate<Procedure<I, T>> {
    val procedure: Once<Procedure<I, T>> = Once()

    return GetDelegate { _, _ ->
        procedure.getOrInit {
            Procedure<I, T>(
                this, name, validator
            ).also { this@procedure.procedures[it.name] = it as Procedure<in Any, out Any> }
        }
    }
}
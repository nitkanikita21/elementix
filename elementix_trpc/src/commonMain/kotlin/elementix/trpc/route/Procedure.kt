package elementix.trpc.route

import io.konform.validation.Validation

expect class Procedure<I: Any?, T: Any?> {
    var route: Route
    var name: String
    var validator: Validation<I>?
}

expect inline fun <reified I: Any?, reified R: Any?> Procedure(
    route: Route,
    name: String,
    validator: Validation<I>?,
): Procedure<I, R>
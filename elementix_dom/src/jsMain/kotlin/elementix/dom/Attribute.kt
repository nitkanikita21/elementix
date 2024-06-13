package elementix.dom

import elementix.dom.view.View
import elementix.reactivity.primitives.ReadSignal

interface Attributes {
    var children: Attribute<List<View>>
}

interface Attribute<T> {
    val data: T
        get() = TODO()

    operator fun invoke() = data

    fun ifNotEquals(other: T, callback: (data: T) -> Unit) {
        if (other != data) callback(data)
    }
}

class StaticAttribute<T>(override val data: T) : Attribute<T>
class ReactiveAttribute<T>(private val dataSignal: ReadSignal<T>) : Attribute<T> {
    override val data: T get() = dataSignal()
}

val <T> T.staticAttribute get() = StaticAttribute(this)
val <T> ReadSignal<T>.reactiveAttribute get() = ReactiveAttribute(this)

fun <T, R> ReadSignal<T>.reactiveAttribute(mapper: (T) -> R) = ReactiveAttribute {
    return@ReactiveAttribute mapper(this())
}

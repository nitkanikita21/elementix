package elementix.dom.view.components.primitive

import elementix.dom.view.View
import elementix.reactivity.primitives.ReadSignal

interface Attributes {
    var children: Attribute<List<View>>
}

sealed interface AttributeValue<T>{
    val data: T
    operator fun invoke() = data
}
class StaticAttributeValue<T>(override val data: T) : AttributeValue<T>
class ReactiveAttributeValue<T>(private val dataSignal: ReadSignal<T>) : AttributeValue<T> {
    override val data: T get() = dataSignal()
}

class Attribute<T> {
    lateinit var value: AttributeValue<T>

    constructor(data: T) {
        set(data)
    }
    constructor(data: ReadSignal<T>) {
        set(data)
    }

    fun set(data: T) { value = StaticAttributeValue<T>(data)}
    fun set(data: ReadSignal<T>) { value = ReactiveAttributeValue<T>(data)}

    infix fun bind(signal: ReadSignal<T>) {
        set(signal)
    }
    operator fun invoke(value: T) { set(value) }
    operator fun invoke(signal: ReadSignal<T>) { set(signal) }

    fun get() = value.data
    operator fun invoke() = value.data

    fun ifNotEquals(other: T, callback: (data: T) -> Unit) {
        if (other != value.data) callback(value.data)
    }
}

/*val <T> T.staticAttribute get() = StaticAttribute(this)
val <T> ReadSignal<T>.reactiveAttribute get() = ReactiveAttribute(this)*/

fun <T, R> ReadSignal<T>.reactiveAttribute(mapper: (T) -> R) = ReactiveAttributeValue {
    return@ReactiveAttributeValue mapper(this())
}

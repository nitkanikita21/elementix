package elementix.dom

import elementix.dom.view.View
import elementix.reactivity.primitives.ReadSignal

interface Props {
    var children: Prop<List<View>>
}

interface Prop<T> {
    val data: T
        get() = TODO()

    operator fun invoke() = data

    fun ifNotEquals(other: T, callback: (data: T) -> Unit) {
        if (other != data) callback(data)
    }
}

class StaticProp<T>(override val data: T) : Prop<T>
class ReactiveProp<T>(private val dataSignal: ReadSignal<T>) : Prop<T> {
    override val data: T get() = dataSignal()
}

val <T> T.staticProp get() = StaticProp(this)
val <T> ReadSignal<T>.reactiveProp get() = ReactiveProp(this)

fun <T, R> ReadSignal<T>.reactiveProp(mapper: (T) -> R) = ReactiveProp {
    return@ReactiveProp mapper(this())
}

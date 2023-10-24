package elementix.reactivity.primitives

fun interface WriteSignal<T> {
    fun set(value: T)
    operator fun invoke(value: T) = set(value)
}


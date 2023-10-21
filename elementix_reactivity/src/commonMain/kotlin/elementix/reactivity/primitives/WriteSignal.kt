package elementix.reactivity.primitives

sealed interface WriteSignal<T> {
    fun set(value: T)
    operator fun invoke(value: T) = set(value)
}
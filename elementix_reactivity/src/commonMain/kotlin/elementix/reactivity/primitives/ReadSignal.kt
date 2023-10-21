package elementix.reactivity.primitives

sealed interface ReadSignal<T> {
    fun get(): T
    operator fun invoke() = get()
}
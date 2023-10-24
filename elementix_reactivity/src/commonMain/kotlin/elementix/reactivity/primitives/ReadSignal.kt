package elementix.reactivity.primitives

fun interface ReadSignal<T> {
    fun get(): T
    operator fun invoke() = get()
}


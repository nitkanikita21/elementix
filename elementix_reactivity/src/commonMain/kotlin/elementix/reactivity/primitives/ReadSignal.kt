package elementix.reactivity.primitives

fun interface ReadSignal<out T> {
    fun get(): T
    operator fun invoke() = get()
}


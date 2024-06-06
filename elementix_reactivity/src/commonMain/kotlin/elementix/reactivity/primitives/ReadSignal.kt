package elementix.reactivity.primitives

fun interface ReadSignal<T> {
    fun get(): T
    fun getUntracked(): T = get()
    operator fun invoke() = get()
}

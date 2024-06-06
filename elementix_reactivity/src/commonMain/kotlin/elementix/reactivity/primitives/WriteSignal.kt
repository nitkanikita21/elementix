package elementix.reactivity.primitives

fun interface WriteSignal<T> {
    fun set(value: T)
    fun setUntracked(value: T) = set(value)
    operator fun invoke(value: T) = set(value)
}


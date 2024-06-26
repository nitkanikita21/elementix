package elementix.reactivity.primitives

interface ReadWriteSignal<T>: ReadSignal<T>, WriteSignal<T> {
    fun set(setter: (previous: T) -> T) {
        set(setter(getUntracked()))
    }

    operator fun invoke(setter: (previous: T) -> T) {
        set(setter(getUntracked()))
    }
}
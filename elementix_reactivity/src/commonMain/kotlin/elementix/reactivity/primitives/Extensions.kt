package elementix.reactivity.primitives

fun <T, S> ReadSignal<T>.map(transform: (T) -> S): ReadSignal<S> = ReadSignal {
    transform(this.get())
}

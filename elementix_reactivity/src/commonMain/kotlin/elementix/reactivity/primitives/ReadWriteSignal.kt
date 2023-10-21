package elementix.reactivity.primitives

sealed interface ReadWriteSignal<T>: ReadSignal<T>, WriteSignal<T> {
}
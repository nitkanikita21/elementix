package elementix.reactivity.primitives

import elementix.reactivity.Context

inline fun <T, S> ReadSignal<T>.map(crossinline transform: (T) -> S): ReadSignal<S> = ReadSignal {
    transform(this.get())
}

inline fun <T, S> ReadSignal<T>.mapMemo(crossinline transform: (T, S?) -> S): Memo<S> = Context.createMemo {
    transform(this.get(), it)
}
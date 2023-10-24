package elementix.reactivity.primitives

fun <T, S: ReadSignal<out T>> S.toStringSignal(): ReadSignal<String> {
    return ReadSignal {
        return@ReadSignal this.get().toString()
    }
}

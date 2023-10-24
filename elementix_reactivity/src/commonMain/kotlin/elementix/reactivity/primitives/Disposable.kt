package elementix.reactivity.primitives

sealed interface Disposable {
    fun destroy()
}
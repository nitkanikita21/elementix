package elementix.reactivity

class Signal<T> internal constructor(
    private val context: Context,
    private val id: SignalId
) {

    fun get(): T {
        val value = context.signalValues[id.value] as T

        context.runningEffect?.let {  effectId ->
            context.signalSubscribers
                .getOrPut(id) { hashSetOf() }
                .add(effectId)
        }

        return value
    }

    fun set(value: T) {
        context.signalValues[id.value] = value as Any
        context.signalSubscribers[id]?.forEach(context::runEffect)
    }

    operator fun invoke(value: T) = set(value)
    operator fun invoke() = get()



}
package elementix.reactivity.primitives

import elementix.reactivity.Context
import elementix.reactivity.SignalId

class Signal<T> internal constructor(
    private val cx: Context,
    private val id: SignalId
): ReadWriteSignal<T> {
    @Suppress("UNCHECKED_CAST")
    override fun get(): T {
        val value = cx.signalValues[id] as T

        cx.runningEffect?.let { effectId ->
            cx.signalSubscribers
                .getOrPut(id) { hashSetOf() }
                .add(effectId)
        }

        return value
    }

    override fun set(value: T) {
        cx.signalValues[id] = value as Any
        cx.signalSubscribers[id]?.forEach(cx::runEffect)
    }
}
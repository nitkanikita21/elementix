package elementix.reactivity.primitives

import elementix.reactivity.Context
import elementix.reactivity.SignalId

class Signal<T> internal constructor(
    private val id: SignalId
): ReadWriteSignal<T>, Disposable {
    @Suppress("UNCHECKED_CAST")
    override fun get(): T {
        val value = Context.signalValues[id] as T

        Context.runningEffect?.let { effectId ->
            Context.signalSubscribers
                .getOrPut(id) { hashSetOf() }
                .add(effectId)
        }

        return value
    }

    override fun set(value: T) {
        Context.signalValues[id] = value as Any
        Context.signalSubscribers[id]?.forEach(Context::runEffect)
    }

    override fun destroy() {
        Context.signalSubscribers.remove(id)
        Context.signalValues.remove(id)
    }

}
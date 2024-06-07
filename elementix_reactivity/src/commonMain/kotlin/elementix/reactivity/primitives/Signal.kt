package elementix.reactivity.primitives

import elementix.reactivity.Context
import elementix.reactivity.SignalId

@Suppress("UNCHECKED_CAST")
class Signal<T : Any?> internal constructor(
    private val id: SignalId
): ReadWriteSignal<T>, Disposable {
    override fun get(): T {
        val value = getUntracked()

        Context.runningEffect?.let { effectId ->
            Context.signalSubscribers
                .getOrPut(id) { hashSetOf() }
                .add(effectId)
        }

        return value
    }

    override fun getUntracked(): T = Context.signalValues.toMap()[id] as T


    override fun set(value: T) {
        setUntracked(value)
        Context.signalSubscribers[id]?.toList()?.forEach(Context::runEffect)
    }

    override fun setUntracked(value: T) {
        Context.signalValues[id] = value
    }

    override fun destroy() {
        Context.signalSubscribers.remove(id)
        Context.signalValues.remove(id)
    }

}
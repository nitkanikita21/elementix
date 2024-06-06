package elementix.reactivity.primitives

import elementix.reactivity.Context
import elementix.reactivity.MemoComputation
import elementix.reactivity.SignalId

class Memo<T: Comparable<T>> internal constructor(
    private val computation: MemoComputation<T>
): ReadSignal<T>, Disposable {
    private var prevValue: T? = null
    private lateinit var id: SignalId

    private val effectDisposable: Disposable = Context.createEffect {
        //context.memoValues[id.value] = computation(prevValue)
        if (id == null) {
            id = Context.nextSignalId()
        }
        val newValue = computation(prevValue)
        if (newValue != prevValue) {
            Context.signalValues[id!!] = newValue as Any
            Context.signalSubscribers[id]?.toList()?.forEach(Context::runEffect)
            prevValue = newValue
        }
    }
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

    override fun destroy() {
        Context.signalSubscribers.remove(id)
        Context.signalValues.remove(id)
    }

}
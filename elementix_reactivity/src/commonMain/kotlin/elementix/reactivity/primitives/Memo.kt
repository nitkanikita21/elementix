package elementix.reactivity.primitives

import elementix.reactivity.Context
import elementix.reactivity.MemoComputation
import elementix.reactivity.SignalId

class Memo<T: Comparable<T>> internal constructor(
    private val computation: MemoComputation<T>
): ReadSignal<T>, Disposable {
    private var prevValue: T? = null
    private lateinit var id: SignalId

    init {
        Context.createEffect {
            //context.memoValues[id.value] = computation(prevValue)
            val newValue = computation(prevValue)
            if (newValue != prevValue) {
                if (!(::id.isInitialized)) {
                    id = Context.nextSignalId()
                } else {
                    Context.signalSubscribers[id]?.forEach(Context::runEffect)
                }
                Context.signalValues[id] = newValue as Any
                prevValue = newValue
            }
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
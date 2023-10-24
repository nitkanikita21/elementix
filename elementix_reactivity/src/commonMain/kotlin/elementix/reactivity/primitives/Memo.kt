package elementix.reactivity.primitives

import elementix.reactivity.Context
import elementix.reactivity.MemoComputation
import elementix.reactivity.SignalId

class Memo<T: Comparable<T>> internal constructor(
    private val cx: Context,
    private val computation: MemoComputation<T>
): ReadSignal<T>, Disposable {
    private var prevValue: T? = null
    private lateinit var id: SignalId

    init {
        cx.createEffect {
            //context.memoValues[id.value] = computation(prevValue)
            val newValue = computation(prevValue)
            if (newValue != prevValue) {
                if (!(::id.isInitialized)) {
                    id = cx.nextSignalId()
                } else {
                    cx.signalSubscribers[id]?.forEach(cx::runEffect)
                }
                cx.signalValues[id] = newValue as Any
                prevValue = newValue
            }
        }
    }

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

    override fun destroy() {
        cx.signalSubscribers.remove(id)
        cx.signalValues.remove(id)
    }

}
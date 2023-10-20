package elementix.reactivity

class Memo<T> internal constructor(
    private val context: Context,
    private val computation: MemoComputation<T>
) {
    private var prevValue: T? = null
    private lateinit var id: SignalId

    init {
        context.createEffect {
            //context.memoValues[id.value] = computation(prevValue)
            val newValue = computation(prevValue)
            if (newValue != prevValue) {
                if (!(::id.isInitialized)) {
                    context.signalValues.add(newValue as Any)
                    id = SignalId(context.signalValues.size - 1)
                } else {
                    context.signalValues[id.value] = newValue as Any
                    context.signalSubscribers[id]?.forEach(context::runEffect)
                }
                prevValue = newValue
            }
        }
    }

    fun get(): T {
        val value = context.signalValues[id.value] as T

        context.runningEffect?.let { effectId ->
            context.signalSubscribers
                .getOrPut(id) { hashSetOf() }
                .add(effectId)
        }

        return value
    }

    operator fun invoke() = get()


}
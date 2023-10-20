package elementix.reactivity

object Context {

    internal val signalValues: MutableList<Any> = arrayListOf()

    internal var runningEffect: EffectId? = null
    internal val signalSubscribers: MutableMap<SignalId, MutableSet<EffectId>> = hashMapOf()
    internal val effects: MutableList<Effect> = arrayListOf()

    fun <T : Any> createSignal(value: T): Signal<T> {
        signalValues.add(value)
        val id = SignalId(signalValues.size - 1)

        return Signal(this, id)
    }

    fun <T: Any> createMemo(computation: MemoComputation<T>): Memo<T> {
        return Memo(
            this, computation
        )
    }

    fun createEffect(effect: Effect) {
        effects.add(effect)
        val id = EffectId(effects.size - 1)
        runEffect(id)
    }

    internal fun runEffect(id: EffectId) {
        val prevRunningEffect = runningEffect?.copy()
        runningEffect = id

        signalSubscribers.forEach { entry ->
            entry.value.remove(runningEffect)
        }

        effects[id.value]()
        runningEffect = prevRunningEffect
    }
}
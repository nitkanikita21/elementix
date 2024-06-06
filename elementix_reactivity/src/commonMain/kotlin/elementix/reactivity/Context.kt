package elementix.reactivity

import elementix.reactivity.primitives.Disposable
import elementix.reactivity.primitives.Memo
import elementix.reactivity.primitives.Signal
import elementix.reactivity.primitives.Trigger
import kotlin.random.Random

object Context {
    internal val signalValues: MutableMap<SignalId, Any> = hashMapOf()
    internal var triggerIds: MutableSet<TriggerId> = hashSetOf()
    internal val effects: MutableMap<EffectId, Effect> = hashMapOf()

    internal var runningEffect: EffectId? = null

    internal val signalSubscribers: MutableMap<SignalId, MutableSet<EffectId>> = hashMapOf()
    internal val triggerSubscribers: MutableMap<TriggerId, MutableSet<EffectId>> = hashMapOf()

    fun <T : Any> createSignal(value: T): Signal<T> {
        val id = nextSignalId()
        signalValues.put(id, value)

        return Signal(id)
    }

    fun <T: Comparable<T>> createMemo(computation: MemoComputation<T>): Memo<T> {
        return Memo(computation)
    }

    fun createTrigger(): Trigger {
        val id = nextTriggerId()
        triggerIds.add(id)
        return Trigger(id)
    }

    fun createEffect(effect: Effect): Disposable {
        val id = nextEffectId()
        effects[id] = effect
        runEffect(id)
        return Disposable {
            signalSubscribers.forEach { (_, set) ->
                set.remove(id)
            }
            triggerSubscribers.forEach { (_, set) ->
                set.remove(id)
            }
            effects.remove(id)
        }
    }

    internal fun runEffect(id: EffectId) {
        val prevRunningEffect = runningEffect?.let { EffectId(it.value) }
        runningEffect = id

        //Dont comment this lines
        signalSubscribers.forEach { entry ->
            entry.value.remove(runningEffect)
        }

        //(OLD) Consider whether this piece of code is necessary.
        //(OLD) It clears the past effect with the same aide, but also clears itself when summoned

        effects[id]?.invoke()
        runningEffect = prevRunningEffect

    }

    //ids

    private fun nextTriggerId(): TriggerId {
        var id = TriggerId(Random.nextInt())
        while (triggerSubscribers.containsKey(id)) {
            id = TriggerId(Random.nextInt())
        }
        return id
    }

    internal fun nextSignalId(): SignalId {
        var id = SignalId(Random.nextInt())
        while (signalValues.containsKey(id)) {
            id = SignalId(Random.nextInt())
        }
        return id
    }

    private fun nextEffectId(): EffectId {
        var id = EffectId(Random.nextInt())
        while (effects.containsKey(id)) {
            id = EffectId(Random.nextInt())
        }
        return id
    }
}
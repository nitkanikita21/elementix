package elementix.reactivity.primitives

import elementix.reactivity.Context
import elementix.reactivity.TriggerId

class Trigger internal constructor(
    private val id: TriggerId
): Disposable {
    fun subscribe() {
        Context.runningEffect?.let { effectId ->
            Context.triggerSubscribers
                .getOrPut(id) { hashSetOf() }
                .add(effectId)
        }
    }

    fun fire() {
        Context.triggerSubscribers[id]?.forEach(Context::runEffect)
    }

    override fun destroy() {
        Context.triggerSubscribers.remove(id)
        Context.triggerIds.remove(id)
    }
}
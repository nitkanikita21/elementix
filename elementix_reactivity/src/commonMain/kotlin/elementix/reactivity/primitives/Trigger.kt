package elementix.reactivity.primitives

import elementix.reactivity.Context
import elementix.reactivity.TriggerId

class Trigger internal constructor(
    private val cx: Context,
    private val id: TriggerId
): Disposable {
    fun subscribe() {
        cx.runningEffect?.let { effectId ->
            cx.triggerSubscribers
                .getOrPut(id) { hashSetOf() }
                .add(effectId)
        }
    }

    fun fire() {
        cx.triggerSubscribers[id]?.forEach(cx::runEffect)
    }

    override fun destroy() {
        cx.triggerSubscribers.remove(id)
        cx.triggerIds.remove(id)
    }
}
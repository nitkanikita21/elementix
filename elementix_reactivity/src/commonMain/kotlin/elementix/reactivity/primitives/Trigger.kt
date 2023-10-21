package elementix.reactivity.primitives

import elementix.reactivity.Context
import elementix.reactivity.TriggerId

class Trigger internal constructor(
    private val cx: Context,
    private val id: TriggerId
){
    fun subscribe() {
        cx.runningEffect?.let { effectId ->
            cx.triggerSubscribers
                .getOrPut(id) { hashSetOf() }
                .add(effectId)
        }
    }

    fun notify() {
        cx.triggerSubscribers[id]?.forEach(cx::runEffect)
    }
}
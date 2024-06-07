package elementix.reactivity

import kotlin.jvm.JvmInline
import kotlin.random.Random

sealed interface Id {
    val value: Int
}

@JvmInline
value class EffectId internal constructor(override val value: Int): Id
@JvmInline
value class SignalId internal constructor(override val value: Int): Id
@JvmInline
value class TriggerId internal constructor(override val value: Int): Id

typealias Effect = () -> Unit
typealias MemoComputation<T> = (T?) -> T
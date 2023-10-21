package elementix.reactivity

import kotlin.random.Random

sealed interface Id {
    val value: Int
}

data class EffectId internal constructor(override val value: Int): Id
data class SignalId internal constructor(override val value: Int): Id
data class TriggerId internal constructor(override val value: Int): Id

typealias Effect = () -> Unit
typealias MemoComputation<T> = (T?) -> T
package elementix.reactivity

data class EffectId internal constructor(internal val value: Int)
data class SignalId internal constructor(internal val value: Int)

typealias Effect = () -> Unit
typealias MemoComputation<T> = (T?) -> T
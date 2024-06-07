package elementix.trpc.hook

import elementix.reactivity.Context
import elementix.reactivity.primitives.Disposable
import elementix.reactivity.primitives.ReadSignal
import elementix.reactivity.primitives.Signal
import elementix.reactivity.primitives.Trigger
import elementix.trpc.route.Procedure
import kotlinx.browser.window
import kotlin.time.Duration

class TrpcHook<I : Any?, R : Any?> internal constructor(
    private val procedure: Procedure<I, R>,
    private val input: ReadSignal<I>,
    private val refetchDelay: Duration
) : Disposable {
    private val fetchedValue: Signal<Result<R>?> = Context.createSignal(null)
    private val refetchTrigger: Trigger = Context.createTrigger()
    private val fetchEffect: Disposable = Context.createEffect {
        refetchTrigger.subscribe()
        fetch()
    }


    val value: Result<R> get() = fetchedValue.get() ?: throw NullPointerException("Value is null")
    val isFetched: Boolean get() = fetchedValue.get() != null

    fun fetch() {
        procedure.call(input.get()).then {
            fetchedValue.set(it)
        }.catch { throw it }
    }

    override fun destroy() {
        fetchedValue.destroy()
        refetchTrigger.destroy()
        fetchEffect.destroy()
    }

    init {
        if (refetchDelay > Duration.ZERO) {
            window.setInterval({
                refetchTrigger.fire()
            }, refetchDelay.inWholeMilliseconds.toInt())
        }
    }
}

fun <I : Any?, R : Any?> createTrpcCall(
    procedure: Procedure<I, R>,
    input: ReadSignal<I>,
    refetchDelay: Duration = Duration.ZERO
): TrpcHook<I, R> =
    TrpcHook(procedure, input, refetchDelay)
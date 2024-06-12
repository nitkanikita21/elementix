package elementix.dom.view.components.custom

import elementix.dom.view.Container
import elementix.utils.GetDelegate

//infix fun <T : Disposable> FunctionalComponent<*>.use(disposable: T): T = useDisposable(disposable)


fun interface FunctionalComponentGetter<P> {
    fun withContent(props: P, slotConstructor: FunctionalComponent<*>.Slot.() -> Unit): FunctionalComponent<P>
}

operator fun <P : Any> FunctionalComponentGetter<P>.invoke(
    props: P
): FunctionalComponent<P> = withContent(props) {}

operator fun <P : Any> FunctionalComponentGetter<P>.invoke(
    props: P, slotConstructor: FunctionalComponent<*>.Slot.() -> Unit
): FunctionalComponent<P> = withContent(props, slotConstructor)

operator fun FunctionalComponentGetter<Unit>.invoke(): FunctionalComponent<Unit> =
    withContent(Unit) {}

operator fun FunctionalComponentGetter<Unit>.invoke(slotConstructor: FunctionalComponent<*>.Slot.() -> Unit): FunctionalComponent<Unit> =
    withContent(Unit, slotConstructor)

fun <P> defineComponent(initializer: FunctionalComponent<P>.(P) -> Unit): GetDelegate<Container, FunctionalComponentGetter<P>> =
    GetDelegate delegate@{ container, _ ->
        return@delegate FunctionalComponentGetter<P> get@{ props: P, slotConstructor ->
            return@get FunctionalComponent<P>(props, slotConstructor)
                .apply { initializer(this.props) }
                .also { fc ->
                    container.appendChild(fc)
                }

        }
    }


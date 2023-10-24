package elementix.dom.tags

import elementix.reactivity.Context
import elementix.reactivity.primitives.Trigger
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement


interface Component {
    companion object {
        private var _id: Int = 0
        val id get() = _id++
    }

    val id: Int
    val cx: Context
    val renderTrigger: Trigger
    val parent: Component
    fun render()
    fun init()
}

interface ComponentWithProps<P> {
    val props: P
}
interface ComponentWithNativeParent<E: Element> {
    val nativeParent: ComponentWithNativeParent<out Element>
    val ref: E
}

abstract class NativeComponent<P: DefaultElementProps<E>, E: Element, C: NativeComponent<P, E, C, CC>, CC: ConfigWithProps<P, C>>(
    final override val cx: Context,
    private val config: Configurator<CC>
): Component, ComponentWithProps<P>, ComponentWithNativeParent<E> {
    override val id: Int = Component.id
    lateinit var refElement: E
        private set
    override val renderTrigger: Trigger = cx.createTrigger()

    override fun init() {
        cx.createEffect {
            props.innerHTML.data
            renderTrigger.fire()
        }
        cx.createEffect {
            renderTrigger.subscribe()
            render()
        }
    }

    override fun render() {
        refElement = createTag()

    }

    abstract fun createTag(): E
}

class AppComponent(override val cx: Context, refElement: HTMLElement) : Component, ComponentWithNativeParent<HTMLElement> {
    override val id: Int = Component.id
    override val renderTrigger: Trigger = cx.createTrigger()
    override val parent: Component = this
    override val nativeParent: ComponentWithNativeParent<HTMLElement> = this
    override val ref: HTMLElement = refElement

    override fun render() {

    }

    override fun init() {
        TODO("Not yet implemented")
    }
}


open class Config<C: Component>(val component: C)
open class ConfigWithProps<P, C: Component>(val props: P, component: C): Config<C>(component)
open class ConfigWithChildren<P, C: Component>(props: P, component: C): ConfigWithProps<P, C>(props, component)
typealias Configurator<C> = C.()->Unit
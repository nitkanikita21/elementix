package elementix.dom.tags

import elementix.dom.NativeComponentConfig
import elementix.dom.Prop
import elementix.dom.PropBuilder
import elementix.reactivity.Context
import kotlinx.browser.document
import org.w3c.dom.Element
import org.w3c.dom.Node

abstract class Component<P>(
    protected val cx: Context
) {
    companion object {
        private var id: Int = 0
    }
    val id = Companion.id++

    abstract val props: P

    abstract fun render(root: Element)
    abstract fun remove()
}

abstract class NativeComponent<P, E : Element>(
    cx: Context,
    configBuilder: NativeComponentConfig<P>.() -> Unit = {}
): Component<P>(cx) {

    abstract val element: E
    protected abstract val config: NativeComponentConfig<P>

    override fun render(root: Element) {
        root.appendChild(this.element)
    }

    override fun remove() {
        element.remove()
    }

}

abstract class CustomComponent<P>(
    cx: Context,
    propsConfig: PropBuilder<P>
): Component<P>(cx) {

}

/*
class ExampleElement(
    cx: Context,
    configBuilder: ComponentConfig<DefaultElementProps<HTMLDivElement>, HTMLDivElement>.() -> Unit = {}
) : InternalComponent<DefaultElementProps<HTMLDivElement>, HTMLDivElement>(cx, configBuilder) {

    override val element: HTMLDivElement = document.createElement("div") as HTMLDivElement
    override val config: ComponentConfig<DefaultElementProps<HTMLDivElement>, HTMLDivElement> =
        ComponentConfig(this).apply(configBuilder)
    override val props = DefaultElementProps<HTMLDivElement>(element).apply(config.propsBuilder)

    init {
        propsEffect()
    }
}
*/


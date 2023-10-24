package elementix.dom

import elementix.dom.tags.Component
import elementix.dom.tags.NativeComponent
import org.w3c.dom.Element
import org.w3c.dom.Node


open class ElementBuilder(
    private val root: Element
) {
    fun add(element: Node) {
        root.appendChild(element)
    }

    fun add(component: Component<*>) {
        component.render(root)
    }

    operator fun Node.unaryPlus() {
        add(this)
    }

    operator fun Component<*>.unaryPlus() {
        add(this)
    }
}

typealias PropBuilder<T> = T.() -> Unit

class NativeComponentConfig<P>(
    private val root: NativeComponent<P, *>
) : ElementBuilder(root.element) {
    internal var propsBuilder: PropBuilder<P> = {}
    fun props(propsBuilder: PropBuilder<P>) {
        this.propsBuilder = propsBuilder
    }
}

/*class FunctionalComponentConfig<P>(
    private val root: NativeComponent<P, *>
) {
    internal var propsBuilder: PropBuilder<P> = {}
    fun props(propsBuilder: PropBuilder<P>) {
        this.propsBuilder = propsBuilder
    }
}*/


fun <E : Element> E.elementBuilder(builder: ElementBuilder.() -> Unit) = ElementBuilder(this).builder()
fun <E : Element> E.elementBuilder(): ElementBuilder = ElementBuilder(this)
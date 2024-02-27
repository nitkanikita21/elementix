package elementix.dom.view.components.primitive

import elementix.dom.asProp
import elementix.dom.view.Component
import elementix.dom.view.Container
import elementix.dom.view.View
import elementix.reactivity.Context
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node

sealed class ElementComponent<E: HTMLElement, P: DefaultElementProps<E>> : Component<P> {
    abstract val element: E

    sealed class Tag(name: String) :
        ElementComponent<HTMLElement, DefaultHTMLElementProps<HTMLElement>>(),
        Container {

        final override val element: HTMLElement = document.createElement(name) as HTMLElement
        final override val props: DefaultHTMLElementProps<HTMLElement> = DefaultHTMLElementProps(element)

        override fun render(parent: Node) {
            props.initEffects(Context)
            parent.appendChild(element)

            props.children().forEach {
                it.render(element)
            }
        }

        override fun appendChild(vararg components: View) {
            props.children = (props.children() + components).asProp()
        }
    }
    sealed class ClosedTag(name: String): ElementComponent<HTMLElement, DefaultHTMLElementProps<HTMLElement>>() {
        final override val element: HTMLElement = document.createElement(name) as HTMLElement
        final override val props: DefaultHTMLElementProps<HTMLElement> = DefaultHTMLElementProps(element)

        override fun render(parent: Node) {
            parent.appendChild(element)
        }
    }
}
class Tag<D : View>(private val parent: Container, private val getter: () -> D) {
    operator fun invoke() {
        parent.appendChild(getter())
    }
}
class OpenedTag<D : View>(private val parent: Container, private val getter: () -> D) {
    operator fun invoke(scope: D.() -> Unit = {}) {
        val element = getter().apply(scope)
        parent.appendChild(element)
    }
}


class Div: ElementComponent.Tag("div")
class Span: ElementComponent.Tag("span")
class Button: ElementComponent.Tag("button")
class H1: ElementComponent.Tag("h1")
class Br: ElementComponent.ClosedTag("br")


val Container.button: OpenedTag<Button> get() = OpenedTag(this) { Button() }
val Container.div: OpenedTag<Div> get() = OpenedTag(this) { Div() }
val Container.span: OpenedTag<Span> get() = OpenedTag(this) { Span() }
val Container.h1: OpenedTag<H1> get() = OpenedTag(this) { H1() }
val Container.br: Tag<Br> get() = Tag(this) { Br() }

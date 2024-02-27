package elementix.dom.tags

import elementix.dom.asProp
import elementix.dom.view.Component
import elementix.dom.view.Container
import elementix.dom.view.View
import elementix.reactivity.Context
import kotlinx.browser.document
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node

sealed class ElementComponent<E: HTMLElement, P: DefaultElementProps<E>> : Component<P> {
    abstract val element: E

    sealed class ContainerTag(name: String) :
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

class Div: ElementComponent.ContainerTag("div")
class Span: ElementComponent.ContainerTag("span")
class Button: ElementComponent.ContainerTag("button")
class H1: ElementComponent.ContainerTag("h1")

class Br: ElementComponent.ClosedTag("br")


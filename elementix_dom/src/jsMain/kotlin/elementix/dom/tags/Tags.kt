package elementix.dom.tags

import elementix.dom.asProp
import elementix.dom.view.Component
import elementix.dom.view.Container
import elementix.reactivity.Context
import kotlinx.browser.document
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

abstract class ElementComponent<E: HTMLElement, P: DefaultElementProps<E>> : Component<P> {
    abstract val element: E

    open class Impl(name: String) : ElementComponent<HTMLElement, DefaultHTMLElementProps<HTMLElement>>() {

        final override val element: HTMLElement = document.createElement(name) as HTMLElement
        final override val props: DefaultHTMLElementProps<HTMLElement> = DefaultHTMLElementProps(element)

        override fun render(parent: Element) {
            props.initEffects(Context)
            parent.appendChild(element)

            props.children().forEach {
                it.render(element)
            }
        }

        override fun appendChild(vararg components: Container) {
            props.children = (props.children() + components).asProp()
        }
    }
}

class Div: ElementComponent.Impl("div")
class Span: ElementComponent.Impl("span")


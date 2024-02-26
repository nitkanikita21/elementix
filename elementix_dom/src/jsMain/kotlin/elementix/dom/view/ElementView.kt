package elementix.dom.view

import elementix.dom.tags.DefaultHTMLElementProps
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node

class ElementView(
    val name: String,
    element: HTMLElement
): View {
    override var mountableNode: Node = element
    override var openingNode: Node = element
    override var closingNode: Node = element
}

fun HTMLElement.intoView(): View = ElementView(this.nodeName, this)
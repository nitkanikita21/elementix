package elementix.dom.view

import kotlinx.browser.document
import org.w3c.dom.Node

class TextView(
    val node: Node
) : View {
    val content: String? = node.textContent
    override var mountableNode: Node = node
    override var openingNode: Node = node
    override var closingNode: Node = node
}

fun String.intoView(): View = TextView(document.createTextNode(this))
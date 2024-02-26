package elementix.dom.view

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node

sealed interface Mountable {
    var mountableNode: Node
    var openingNode: Node
    var closingNode: Node
}

fun mountTo(
    parent: HTMLElement,
    viewBuilder: () -> IntoView
) {
    val view = viewBuilder().intoView()
    parent.appendChild(view.mountableNode)

}
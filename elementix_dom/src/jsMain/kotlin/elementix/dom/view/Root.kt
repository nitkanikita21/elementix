package elementix.dom.view

import kotlinx.browser.document
import org.w3c.dom.Element
import org.w3c.dom.Node

class Root: Container {
    private val children: MutableList<View> = mutableListOf()

    override fun render(parent: Node) {
        children.forEach { it.render(parent) }
    }

    override fun appendChild(vararg components: View) {
        children.addAll(components)
    }
}
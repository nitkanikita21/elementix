package elementix.dom.view.components

import elementix.dom.view.Container
import elementix.dom.view.View
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
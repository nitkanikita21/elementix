package elementix.dom.view

import kotlinx.browser.document
import org.w3c.dom.Element

class Root: Container {
    private val children: MutableList<Container> = mutableListOf()

    override fun render(parent: Element) {
        children.forEach { it.render(parent) }
    }

    override fun appendChild(vararg components: Container) {
        children.addAll(components)
    }
}
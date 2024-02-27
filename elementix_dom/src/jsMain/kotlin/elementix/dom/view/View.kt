package elementix.dom.view

import org.w3c.dom.Node

interface View {
    fun render(parent: Node)
}
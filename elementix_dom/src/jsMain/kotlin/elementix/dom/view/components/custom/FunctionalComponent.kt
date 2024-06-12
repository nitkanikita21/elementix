package elementix.dom.view.components.custom

import elementix.dom.view.Component
import elementix.dom.view.Container
import elementix.dom.view.View
import org.w3c.dom.Node

class FunctionalComponent<P>(
    override val props: P,
    private val slotConstructor: FunctionalComponent<*>.Slot.() -> Unit
) : Component<P>, Container {

    inner class Slot: Container {
        private val children: MutableList<View> = mutableListOf()

        override fun appendChild(vararg components: View) {
            children.addAll(components)
        }

        override fun render(parent: Node) {
            children.forEach { it.render(parent) }
        }
    }

    private val children: MutableList<View> = mutableListOf()

    val Container.slot get() = Slot().apply(slotConstructor).also { this.appendChild(it) }

    override fun appendChild(vararg components: View) {
        children.addAll(components)
    }

    override fun render(parent: Node) {
        children.forEach { it.render(parent) }
    }

}

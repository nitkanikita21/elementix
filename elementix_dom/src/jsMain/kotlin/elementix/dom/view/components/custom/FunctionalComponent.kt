package elementix.dom.view.components.custom

import elementix.dom.view.Component
import elementix.dom.view.Container
import elementix.dom.view.View
import org.w3c.dom.Node

class FunctionalComponent<P>(
    override val props: P,
    private val defaultSlotConstructor: SlotConstructor
) : Component<P>, Container, SlotsContainerReadOnly, SlotsContainerWriteOnly {

    inner class Slot(slotsContainerWriteOnly: SlotsContainerWriteOnly) : Container,
        SlotsContainerWriteOnly by slotsContainerWriteOnly {

        private val children: MutableList<View> = mutableListOf()

        override fun appendChild(vararg components: View) {
            children.addAll(components)
        }

        override fun render(parent: Node) {
            children.forEach { it.render(parent) }
        }
    }


    private val children: MutableList<View> = mutableListOf()

    private val slots: MutableMap<String, SlotConstructor> = mutableMapOf()

//    val Container.slot get() = Slot().apply(defaultSlotConstructor).also { this.appendChild(it) }

    override fun appendChild(vararg components: View) {
        children.addAll(components)
    }

    override fun render(parent: Node) {
        children.forEach { it.render(parent) }
    }

    override fun Container.slot(id: String): Slot {
        console.log("Getting slot for $id")
        return Slot(this@FunctionalComponent)
            .apply(slots[id] ?: {})
            .also {
                console.log("Appending slot ${id} to ${this@slot::class.simpleName}")
                this@slot.appendChild(it)
            }
    }

    override fun slot(id: String, constructor: SlotConstructor) {
        console.log("Setting slot constructor for $id")
        slots[id] = constructor
    }

    init {
        console.log("Initializing default slot")
        slot("default", defaultSlotConstructor)
        slot()
    }

}

typealias SlotConstructor = FunctionalComponent<*>.Slot.() -> Unit

interface SlotsContainerWriteOnly {
    fun slot(id: String, constructor: SlotConstructor)
}

interface SlotsContainerReadOnly {
    fun Container.slot(id: String = "default"): FunctionalComponent<*>.Slot
}
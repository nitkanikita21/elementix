package elementix.dom.view

import elementix.dom.Props
import elementix.reactivity.primitives.ReadSignal
import org.w3c.dom.Element
import org.w3c.dom.Node

interface View {
    fun render(parent: Node)
}

interface Container: View {
    fun appendChild(vararg components: View)

    operator fun Text.unaryPlus() {
        appendChild(this)
    }

    operator fun Any.unaryPlus() {
        appendChild(Text { this.toString() })
    }

    operator fun ReadSignal<String>.unaryPlus() {
        appendChild(Text(this))
    }
}

interface Component<P: Props>: View {
    val props: P
}


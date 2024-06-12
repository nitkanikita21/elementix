package elementix.dom.view

import elementix.dom.view.components.primitive.Text
import elementix.reactivity.primitives.ReadSignal

interface Container: View {
    fun appendChild(vararg components: View)

    operator fun Text.unaryPlus() {
        appendChild(this)
    }

    operator fun String.unaryPlus() {
        appendChild(Text { this })
    }

    operator fun ReadSignal<String>.unaryPlus() {
        appendChild(Text(this))
    }
}

operator fun <C: Container> C.invoke(scope: C.() -> Unit) = this.apply(scope)

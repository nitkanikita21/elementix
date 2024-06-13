package elementix.dom.view.components.meta

import elementix.dom.view.Container
import elementix.dom.view.View
import org.w3c.dom.Node

class App: Container {
    private val children: MutableList<View> = mutableListOf()

    override fun render(parent: Node) {
        children.forEach { it.render(parent) }
    }

    override fun appendChild(vararg components: View) {
        children.addAll(components)
    }
}


fun createApp(initialize: App.() -> Unit = {}): App = App().apply(initialize)
fun renderApp(parent: Node, initialize: App.() -> Unit = {}) = App().apply(initialize).render(parent)
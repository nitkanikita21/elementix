package elementix.dom.view.components.meta

import elementix.dom.removeNodesBetweenAnchors
import elementix.dom.view.Container
import elementix.dom.view.View
import elementix.reactivity.Context
import elementix.reactivity.primitives.ReadSignal
import elementix.reactivity.primitives.map
import kotlinx.browser.document
import org.w3c.dom.DocumentFragment
import org.w3c.dom.Node

fun <T> Container.viewFor(listSignal: ReadSignal<List<T>>, generator: Container.(index: Int, elem: T) -> Unit) {
    this.appendChild(For(listSignal, generator))
}
fun Container.viewFor(countSignal: ReadSignal<Int>, generator: Container.(index: Int) -> Unit) {
    this.appendChild(For(countSignal.map { List(it) { it } }) { i, _ -> generator(i) })
}

class For<T>(
    val listSignal: ReadSignal<List<T>>,
    val generator: Container.(index: Int, elem: T) -> Unit
) : View {
    inner class ForContainer : Container {
        internal val children: MutableList<View> = mutableListOf()
        override fun appendChild(vararg components: View) {
            children.addAll(components)
        }

        override fun render(parent: Node) {
            children.forEach { it.render(parent) }
        }

    }

    private var isRendered = false
    private val anchorStart = document.createComment("for start")
    private val anchorEnd = document.createComment("for end")

    private val innerInstance = ForContainer()


    override fun render(parent: Node) {
        var fragment: DocumentFragment = document.createDocumentFragment()
        Context.createEffect {
            if (!isRendered) {
                parent.appendChild(anchorStart)
                renderChildren(fragment)
                parent.appendChild(fragment)
                parent.appendChild(anchorEnd)
                isRendered = true
                console.log("first render", fragment)
            } else {
                fragment = document.createDocumentFragment()
                removeNodesBetweenAnchors(parent, anchorStart, anchorEnd)
                renderChildren(fragment)
                parent.insertBefore(fragment, anchorEnd)
                console.log("rerender")

            }
        }
    }

    private fun renderChildren(parent: Node) {
        innerInstance.children.clear()
        listSignal().forEachIndexed { index, it ->
            innerInstance.generator(index, it)
            console.log()
        }
        innerInstance.render(parent)
    }

}

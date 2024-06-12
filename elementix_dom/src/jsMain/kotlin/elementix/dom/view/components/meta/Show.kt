package elementix.dom.view.components.meta

import elementix.dom.removeNodesBetweenAnchors
import elementix.dom.view.Container
import elementix.dom.view.View
import elementix.reactivity.Context
import elementix.reactivity.primitives.ReadSignal
import kotlinx.browser.document
import org.w3c.dom.DocumentFragment
import org.w3c.dom.Node

fun Container.viewShow(whenSignal: ReadSignal<Boolean>, scope: Container.() -> Unit) {
    this.appendChild(Show(whenSignal, scope))
}

class Show(
    val whenSignal: ReadSignal<Boolean>,
    val scope: Container.() -> Unit
) : View {
    inner class ShowContainer : Container {
        private val children: MutableList<View> = mutableListOf()
        override fun appendChild(vararg components: View) {
            children.addAll(components)
        }

        override fun render(parent: Node) {
            children.forEach { it.render(parent) }
        }

    }

    private var isRendered = false
    private var previousState: Boolean? = null
    private val anchorStart = document.createComment("show start")
    private val anchorEnd = document.createComment("show end")

    private val innerInstance = ShowContainer().apply(scope)

    override fun render(parent: Node) {
        var fragment: DocumentFragment = document.createDocumentFragment()
        Context.createEffect {
            if (!isRendered) {
                parent.appendChild(anchorStart)
                if(whenSignal()) {
                    renderChildren(fragment)
                    parent.appendChild(fragment)
                }
                parent.appendChild(anchorEnd)
                isRendered = true
                console.log("first render", fragment)
            } else {
                if(!whenSignal() && (previousState == true || previousState == null)  ){
                    removeNodesBetweenAnchors(parent, anchorStart, anchorEnd)
                } else if(whenSignal()&& (previousState == false || previousState == null) ) {
                    fragment = document.createDocumentFragment()
                    removeNodesBetweenAnchors(parent, anchorStart, anchorEnd)
                    renderChildren(fragment)
                    parent.insertBefore(fragment, anchorEnd)
                }
                console.log("show rerender")

            }
        }
    }

    private fun renderChildren(parent: Node) {
        innerInstance.render(parent)
    }


}

package elementix.dom.view

import elementix.reactivity.Context
import elementix.reactivity.primitives.ReadSignal
import kotlinx.browser.document
import org.w3c.dom.Element
import org.w3c.dom.Node

class Text(private val textSignal: ReadSignal<String>): View {
    private var rendered = false
    override fun render(parent: Node) {
        var oldNode: Node? = null
        Context.createEffect {
            if(!rendered) rendered = true
            val newNode = document.createTextNode(textSignal())
            if(oldNode != null) {
                (oldNode.asDynamic()).replaceWith(newNode)
            } else {
                parent.appendChild(newNode)
            }
            oldNode = newNode;
        }
    }
}


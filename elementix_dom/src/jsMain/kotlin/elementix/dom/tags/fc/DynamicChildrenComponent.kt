package elementix.dom.tags.fc

import elementix.dom.tags.Component
import elementix.dom.tags.CustomComponent
import elementix.reactivity.Context
import kotlinx.browser.document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.get

abstract class DynamicChildrenComponent<P>(cx: Context, propsConfig: P.() -> Unit) :
    CustomComponent<P>(cx, propsConfig) {

        val renderChildren: MutableList<Component<*>> = mutableListOf()


    override fun render(root: Element) {
        cx.createEffect {
            console.log("effect")
            val nodes = getNodes()

            /*if(!marker.isConnected){
                root.appendChild(marker)
            }
            var lastNode: Node = marker
            for (component in nodes.reversed()) {
                component.render(root)
                lastNode = component.marker
            }*/
            if (root.innerHTML.contains("<!--${startCommentContent}-->")) {

                renderChildren.forEach { it.remove() }
                renderChildren.clear()

                val tempTag = document.createElement("div")
                document.head!!.appendChild(tempTag)

                nodes.forEach { it.render(root); renderChildren.add(it) }


                root.innerHTML = root.innerHTML.replace(
                    Regex("<!--$startCommentContent-->[\\s\\S]*?<!--$endCommentContent-->", RegexOption.MULTILINE),
                    tempTag.outerHTML
                )

            } else {
                root.appendChild(startComment)
                renderChildren.clear()
                nodes.forEach { it.render(root); renderChildren.add(it) }
                root.appendChild(endComment)
            }

        }
    }

    override fun remove() {
        renderChildren.forEach { it.remove() }
        renderChildren.clear()
    }

    abstract fun getNodes(): List<Component<*>>

    val startCommentContent = "start dynamic child $id"
    val endCommentContent = "end dynamic child $id"

    val startComment = document.createComment(startCommentContent)
    val endComment = document.createComment(endCommentContent)
}
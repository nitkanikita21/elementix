package elementix.dom

import org.w3c.dom.Node
import org.w3c.dom.asList

fun removeNodesBetweenAnchors(parent: Node, anchorStart: Node, anchorEnd: Node) {
    val list = parent.childNodes.asList()
    val indexStart = list.indexOf(anchorStart)
    val indexEnd = list.indexOf(anchorEnd)
    list.slice((indexStart + 1)..<indexEnd).forEach {
        console.log("removing", it)
        parent.removeChild(it)
    }
}
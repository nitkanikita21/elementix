package elementix.dom.view

import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node

class ElementDescriptor<E: HTMLElement>(
    val element: E
) {
    fun child(child: IntoView): ElementDescriptor<E> {
        val childView = child.intoView()
        mountChild(MountKind.Append(this.element), childView)
        return this
    }
}

fun mountChild(kind: MountKind, child: Mountable) {
    val mountableChild = child.mountableNode
    when (kind) {
        is MountKind.Append -> kind.element.appendChild(mountableChild)
        is MountKind.Before -> (kind.closing as Element).before(mountableChild)
    }
}

sealed interface MountKind {
    class Append(val element: Node): MountKind
    class Before(val closing: Node): MountKind
}

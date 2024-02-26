package elementix.dom.view

import elementix.dom.Props
import org.w3c.dom.Element

interface Container {
    fun render(parent: Element)
    fun appendChild(vararg components: Container)
}

interface Component<P: Props>: Container {
    val props: P
}


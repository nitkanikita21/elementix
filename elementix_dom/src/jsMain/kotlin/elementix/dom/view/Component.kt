package elementix.dom.view

import elementix.dom.Props

interface Component<P: Props>: View {
    val props: P
    fun defineProps(scope: P.() -> Unit) {
        props.apply(scope)
    }
}


package elementix.dom

import kotlinx.html.FlowContent
import kotlinx.html.TagConsumer
import org.w3c.dom.HTMLElement

class FC<P : Any>(
    val view: IntoView<P, *>
) {
    operator fun invoke(props: P): ComponentAppender<*> {
        return {
            val element = view(it, props)


        }
    }
}

fun TagConsumer<HTMLElement>.component(ca: ComponentAppender<*>) {
    ca(this)
}

fun FlowContent.component(ca: ComponentAppender<*>) {
    ca(this.consumer)
}

infix fun TagConsumer<HTMLElement>.injectFC(ca: ComponentAppender<*>) {
    ca(this)
}

infix fun FlowContent.injectFC(ca: ComponentAppender<*>) {
    ca(this.consumer)
}
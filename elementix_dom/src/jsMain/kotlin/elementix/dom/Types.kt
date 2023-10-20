package elementix.dom

import kotlinx.html.TagConsumer
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

typealias IntoView<P, E> = TagConsumer<E>.(props: P) -> E

typealias ComponentAppender<E> = (TagConsumer<E>) -> Unit
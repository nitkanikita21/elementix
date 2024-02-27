package elementix.dom.view

import elementix.dom.tags.*

val Container.button: OpenedTag<Button> get() = OpenedTag(this) { Button() }
val Container.div: OpenedTag<Div> get() = OpenedTag(this) { Div() }
val Container.span: OpenedTag<Span> get() = OpenedTag(this) { Span() }
val Container.h1: OpenedTag<H1> get() = OpenedTag(this) { H1() }
val Container.br: Tag<Br> get() = Tag(this) { Br() }


class Tag<D : View>(val parent: Container, val getter: () -> D) {
    init {
        parent.appendChild(getter())
    }
}


class OpenedTag<D : View>(val parent: Container, val getter: () -> D) {
    operator fun invoke(scope: D.() -> Unit) {
        val element = getter().apply(scope)
        parent.appendChild(element)
    }
}



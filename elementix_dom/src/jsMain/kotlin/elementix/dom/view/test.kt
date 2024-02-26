package elementix.dom.view

import elementix.dom.tags.Div
import elementix.dom.tags.Span

fun Container.div(scope: Div.() -> Unit) {
    val div = Div().apply(scope)
    appendChild(div)
}

fun Div.span(scope: Span.() -> Unit) {
    val div = Span().apply(scope)
    appendChild(div)
}




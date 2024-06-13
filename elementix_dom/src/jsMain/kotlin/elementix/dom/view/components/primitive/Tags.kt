package elementix.dom.view.components.primitive

import elementix.dom.view.Component
import elementix.dom.view.Container
import elementix.dom.view.View
import elementix.reactivity.Context
import kotlinx.browser.document
import org.w3c.dom.*
import kotlin.reflect.KProperty

sealed class ElementComponent<E : HTMLElement, P : DefaultElementAttributes<E>> : Component<P> {
    abstract val element: E

    class Tag<T : HTMLElement, P : DefaultHTMLElementAttributes<T>>(
        name: String, propsProvider: (T) -> P
    ) : ElementComponent<T, P>(), Container {

        final override val element: T = document.createElement(name) as T
        final override val props: P = propsProvider(element)

        override fun render(parent: Node) {
            props.initEffects(Context)
            parent.appendChild(element)

            props.children().forEach {
                it.render(element)
            }
        }

        override fun appendChild(vararg components: View) {
            props.children(props.children() + components)
        }
    }

    class ClosedTag<T : HTMLElement, P : DefaultHTMLElementAttributes<T>>(
        name: String, propsProvider: (T) -> P
    ) : ElementComponent<T, P>() {
        final override val element: T = document.createElement(name) as T
        final override val props: P = propsProvider(element)

        override fun render(parent: Node) {
            parent.appendChild(element)


        }

    }
}

fun <P: DefaultElementAttributes<*>> ElementComponent<*, P>.defineAttributes(scope: P.() -> Unit) {
    props.apply(scope)
}

/*class Tag<D : View>(private val parent: Container, private val getter: () -> D) {
    operator fun invoke() {
        parent.appendChild(getter())
    }
}

class OpenedTag<D : View>(private val parent: Container, private val getter: () -> D) {
    operator fun invoke(scope: D.() -> Unit = {}) {
        val element = getter().apply(scope)
        parent.appendChild(element)
    }
}*/

fun interface ReadDelegate<T : Any, V> {
    operator fun getValue(thisRef: T, property: KProperty<*>): V
}


fun <C : Container, E : HTMLElement, P : DefaultHTMLElementAttributes<E>> openHtmlElement(
    name: String? = null,
    propsProvider: (E) -> P
): ReadDelegate<C, ElementComponent.Tag<E, P>> =
    ReadDelegate { thisRef, property ->
        ElementComponent.Tag(name ?: property.name, propsProvider).also { thisRef.appendChild(it) }
    }

fun <C : Container, E : HTMLElement, P : DefaultHTMLElementAttributes<E>> closedHtmlElement(
    name: String? = null,
    propsProvider: (E) -> P
): ReadDelegate<C, ElementComponent.ClosedTag<E, P>> =
    ReadDelegate { thisRef, property ->
        ElementComponent.ClosedTag(name ?: property.name, propsProvider).also { thisRef.appendChild(it) }
    }

val Container.div by openHtmlElement<Container, HTMLDivElement, DefaultHTMLElementAttributes<HTMLDivElement>> { DefaultHTMLElementAttributes(it) }
val Container.span by openHtmlElement<Container, HTMLSpanElement, DefaultHTMLElementAttributes<HTMLSpanElement>> { DefaultHTMLElementAttributes(it) }
val Container.button by openHtmlElement<Container, HTMLButtonElement, DefaultHTMLElementAttributes<HTMLButtonElement>> { DefaultHTMLElementAttributes(it) }
val Container.h1 by openHtmlElement<Container, HTMLHeadingElement, DefaultHTMLElementAttributes<HTMLHeadingElement>> { DefaultHTMLElementAttributes(it) }
val Container.ol by openHtmlElement<Container, HTMLOListElement, DefaultHTMLElementAttributes<HTMLOListElement>> { DefaultHTMLElementAttributes(it) }
val Container.ul by openHtmlElement<Container, HTMLUListElement, DefaultHTMLElementAttributes<HTMLUListElement>> { DefaultHTMLElementAttributes(it) }
val Container.li by openHtmlElement<Container, HTMLLIElement, DefaultHTMLElementAttributes<HTMLLIElement>> { DefaultHTMLElementAttributes(it) }
val Container.br by closedHtmlElement<Container, HTMLBRElement, DefaultHTMLElementAttributes<HTMLBRElement>> { DefaultHTMLElementAttributes(it) }
val Container.hr by closedHtmlElement<Container, HTMLHRElement, DefaultHTMLElementAttributes<HTMLHRElement>> { DefaultHTMLElementAttributes(it) }

/*
class Div : ElementComponent.Tag<HTMLDivElement, DefaultHTMLElementProps<HTMLDivElement>>("div", {
    DefaultHTMLElementProps(it)
})
val Container.div get() = Div().also { appendChild(it) }

class Span : ElementComponent.Tag<HTMLDivElement, DefaultHTMLElementProps<HTMLDivElement>>("span", {
    DefaultHTMLElementProps(it)
})
val Container.span get() = Span().also { appendChild(it) }


class Button : ElementComponent.Tag<HTMLDivElement, DefaultHTMLElementProps<HTMLDivElement>>("button", {
    DefaultHTMLElementProps(it)
})
val Container.button get() = Button().also { appendChild(it) }


class H1 : ElementComponent.Tag<HTMLDivElement, DefaultHTMLElementProps<HTMLDivElement>>("h1", {
    DefaultHTMLElementProps(it)
})
val Container.h1 get() = H1().also { appendChild(it) }


class Br : ElementComponent.ClosedTag<HTMLDivElement, DefaultHTMLElementProps<HTMLDivElement>>("br", {
    DefaultHTMLElementProps(it)
})
val Container.br get() = Br().also { appendChild(it) }

class Ol : ElementComponent.Tag<HTMLOListElement, DefaultHTMLElementProps<HTMLOListElement>>("ol", {
    DefaultHTMLElementProps(it)
})
val Container.ol get() = Ol().also { appendChild(it) }

class Ul : ElementComponent.Tag<HTMLUListElement, DefaultHTMLElementProps<HTMLUListElement>>("ol", {
    DefaultHTMLElementProps(it)
})
val Container.ul get() = Ul().also { appendChild(it) }

class Li : ElementComponent.Tag<HTMLLIElement, DefaultHTMLElementProps<HTMLLIElement>>("li", {
    DefaultHTMLElementProps(it)
})
val Ol.li get() = Li().also { appendChild(it) }
val Ul.li get() = Li().also { appendChild(it) }*/

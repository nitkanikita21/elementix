import elementix.dom.asProp
import elementix.dom.toProp
import elementix.dom.view.*
import elementix.dom.view.components.Root
import elementix.dom.view.components.primitive.*
import elementix.dom.view.components.viewFor
import elementix.dom.view.components.viewShow
import elementix.reactivity.Context
import elementix.reactivity.primitives.ReadSignal
import kotlinx.browser.document
import org.w3c.dom.events.MouseEvent

fun main() {


    val count = Context.createSignal(0)
    val list: ReadSignal<List<Int>> = ReadSignal {
        List(count()) { it }
    }
    val clickText = ReadSignal {
        "CLICKED: ${count()}"
    }

    val toggle = Context.createSignal(false)

    val root: Container = Root()
    root.div {
        h1 {
            +"Counter"
        }

        div {
            props.id = count.toProp(Any::toString)
        }
        button {
            props.className("btn")
            +clickText

            props.onClick = { e: MouseEvent ->
                if (!e.shiftKey) {
                    count(count() + 1)
                } else {
                    count(count() - 1)
                }
            }.asProp()
        }

        h1 {
            +clickText
        }

        h1 {
            +"For"
        }

        viewFor(list) { element, index ->
            div {
                + "$index) "
                + element.toString()
            }
        }

        h1 {
            +"Show"
        }
        button {
            props.onClick = { e: MouseEvent ->
                toggle(!toggle())
            }.asProp()
            +"Click to toggle"
        }
        viewShow(toggle) {
            div {
                +"Show: "
                + ReadSignal {
                    toggle().toString()
                }
                br()
                +clickText
            }
        }
    }

    root.render(document.getElementById("root")!!)

}
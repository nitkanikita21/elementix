import elementix.dom.asProp
import elementix.dom.toProp
import elementix.dom.view.*
import elementix.reactivity.Context
import elementix.reactivity.primitives.ReadSignal
import kotlinx.browser.document
import org.w3c.dom.events.MouseEvent

//val cx = Context()

fun main() {


    val count = Context.createSignal(0)
    val list: ReadSignal<List<Int>> = ReadSignal {
        List(count()) { it }
    }

    val root: Container = Root()

    val clickText = ReadSignal {
        "CLICKED: ${count()}"
    }

    root.div {
        props.className("flex flex-col p-6 gap-2 w-1/3")

        div {
            props.id = count.toProp { it.toString() }
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
        div {
            +"TEXT DIV  "
            br
            +clickText
            +"  END TEXT"
        }

        h1 {
            +"Dynamic FOR"
            props.className("text-6xl")
        }

        viewFor(list) { element, index ->
            div {
                props.className("bg-base-300 text-6xl flex flex-row rounded-lg gap-2 text-center justify-center")
                span {
                    props.className("p-2")
                    + element.toString()
                }
                div {
                    props.className("bg-base-200 p-2 justify-center text-center text-3xl w-full rounded-r-lg")
                    + "Element index "
                    + index.toString()

                }
            }
        }
    }

    root.render(document.getElementById("root")!!)

}
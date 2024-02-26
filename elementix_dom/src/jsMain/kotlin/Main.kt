import elementix.dom.asProp
import elementix.dom.toProp
import elementix.dom.view.Container
import elementix.dom.view.Root
import elementix.dom.view.div
import elementix.dom.view.span
import elementix.reactivity.Context
import elementix.reactivity.primitives.ReadSignal
import kotlinx.browser.document
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.MouseEvent

//val cx = Context()

fun main() {

    val count = Context.createSignal(0)
    val list = ReadSignal {
        mutableListOf<Int>().apply {
            console.log("each")
            for (i in 0..count.get()) {
                add(count.get())
            }
        }
    }

//    val view = (document.createElement("div") as HTMLDivElement).intoView()

    val root: Container = Root()

    val clickText = ReadSignal {
        "CLICKED: ${count()}"
    }

    Context.createEffect {
        console.log(clickText())
    }

    root.div {
        div {
            props.id = count.toProp { it.toString() }
        }
        span {
            props.innerText = clickText.toProp()

            props.onClick = {_ : MouseEvent ->
                count(count() + 1)
            }.asProp()
        }
    }

    root.render(document.getElementById("root")!!)

}
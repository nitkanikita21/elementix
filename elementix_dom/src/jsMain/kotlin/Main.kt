import elementix.dom.view.intoView
import elementix.reactivity.Context
import elementix.reactivity.primitives.ReadSignal
import kotlinx.browser.document
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

//val cx = Context()

fun main() {

    var count = Context.createSignal(0)
    val list = ReadSignal {
        mutableListOf<Int>().apply {
            console.log("each")
            for (i in 0..count.get()) {
                add(count.get())
            }
        }
    }

    val view = (document.createElement("div") as HTMLDivElement).intoView()



}
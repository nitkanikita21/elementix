import elementix.dom.FC
import elementix.dom.injectFC
import elementix.reactivity.Context
import kotlinx.browser.document
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.dom.create
import kotlinx.html.js.html
import kotlinx.html.onClick

fun main() {

    console.log("hello world!")

    val testFC = FC<String> { props ->
        val number = Context.createSignal(0)

        button {
            Context.createEffect {
                +("$props $number | ")
            }
            onClick + {
                number(number()+1)
            }
        }
    }

    document.getElementById("root")?.append {
        this injectFC testFC("hello")
    }
}

import elementix.dom.ReactiveProp
import elementix.dom.StaticProp
import elementix.dom.asProp
import elementix.dom.elementBuilder
import elementix.dom.tags.Button
import elementix.dom.tags.Div
import elementix.dom.tags.P
import elementix.dom.tags.fc.For
import elementix.reactivity.Context
import elementix.reactivity.primitives.ReadSignal
import elementix.reactivity.primitives.toStringSignal
import kotlinx.browser.document
import kotlinx.browser.window
import kotlin.random.Random
import kotlin.random.nextUInt

//val cx = Context()

fun main() {
    val cx = Context()

    var count = cx.createSignal(0)
    val list = ReadSignal {
        mutableListOf<Int>().apply {
            console.log("each")
            for (i in 0..count.get()){
                add(count.get())
            }
        }
    }


    document.getElementById("root")!!.elementBuilder {
        +Button(cx) {
            props {
                onClick = StaticProp {
                    count(count()-1)
                }
                innerText = StaticProp("-")
            }
        }
        +P(cx) {
            props {
                innerText = ReactiveProp(count.toStringSignal())
            }
        }
        +Button(cx) {
            props {
                onClick = StaticProp {
                    count(count()+1)
                }
                innerText = StaticProp("+")
            }
        }

        +For<Int>(cx) {
            each = ReactiveProp(list)
            generator = { it: Int ->
                P(cx) {
                    props { innerText = it.toString().asProp() }
                }
            }.asProp()
        }
        +P(cx) {
            props {
                innerText = "###################################".asProp()
            }
        }
    }

}
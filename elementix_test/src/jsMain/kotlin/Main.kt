import elementix.dom.reactiveAttribute
import elementix.dom.staticAttribute
import elementix.dom.view.Container
import elementix.dom.view.components.custom.defineComponent
import elementix.dom.view.components.custom.invoke
import elementix.dom.view.components.meta.renderApp
import elementix.dom.view.components.meta.viewFor
import elementix.dom.view.components.primitive.*
import elementix.dom.view.invoke
import elementix.reactivity.Context
import elementix.reactivity.primitives.ReadSignal
import elementix.reactivity.primitives.ReadWriteSignal
import elementix.reactivity.primitives.map
import elementix.trpc.route.initializeClientConfig
import io.ktor.http.*
import io.ktor.util.reflect.*
import kotlinx.browser.document
import org.w3c.dom.events.MouseEvent

fun main() {
    println("Hello world!")
    initializeClientConfig {
        backendUrl = Url("http://localhost:8080")
        pluginConfiguration = tRpcPluginConfiguration
    }

    println(typeInfo<TestDTO>())
    println(TestDTO.serializer())

    /*MyRoute.repeatWordProcedure.call("Hello!").then {
        println(it.getOrThrow())
    }

    MyRoute.test.call(TestDTO(2, listOf(1, 234, 542, 55))).then {
        println(it.getOrThrow())
    }*/

    val count = Context.createSignal(0) // Signal
    val clickText = count.map { // Map signal
        "Clicked count: ${it}"
    }

    val list: ReadSignal<List<Int>> = ReadSignal { // Derive signal
        List(count()) { it } // Create a list of numbers whose length is the value of the counter
    }

    renderApp(document.getElementById("root")!!) { // Create an App in the root element
        div {
            /* Sets the value of the prop id of the counter value.
            Due to the fact that we cannot directly set the String
            variable to an Int type value, we convert it to the desired
            type using a mapper */
            defineAttributes {
                id = count.map(Any::toString).reactiveAttribute
                // `ReadSignal#reactiveProp` converts `ReadSignal` to `ReactiveProp`
            }
        }
        button {
            +clickText // We add the reactive text as a text node to the element
            defineAttributes {
                onClick = { e: MouseEvent ->
                    if (!e.shiftKey) {
                        count { it + 1 }
                    } else {
                        count { it - 1 }
                    }
                }.staticAttribute //`Any#staticProp` Turns anything into a Static Prop
            }
        }
        div {
            +"TEXT DIV  " // Adds a text node
            br
            +clickText
        }
        h1 {
            +"Dynamic FOR"
        }
        ol {
            viewFor(list) { element, index -> // `viewFor` generates a list of elements based on a list using a generator function
                li {
                    +count.map { "#".repeat(it) }
                }
            }
        }

        val count = Context.createSignal(0)

        myButton(count)

        h1 {
            +"NEW"
        }

        splittedComponent(count) {

            console.log("Default slot")
            h1 { +"HELLO WORLD" }

            slot("new") {
                div { +"new slot" }
            }
        }
    }

}

val Container.myButton by defineComponent<ReadWriteSignal<Int>> { counter ->
    button {
        defineAttributes {
            onClick = { event: MouseEvent ->
                if (event.shiftKey) {
                    counter { it - 1 }
                } else {
                    counter { it + 1 }
                }
            }.staticAttribute
        }

        +counter.map { "Count ${counter()}" }
    }
}

val Container.splittedComponent by defineComponent { count ->
    h1 { +"Default slot" }
    viewFor(count) {
        slot()
        hr
    }
    hr
    slot("new")
}


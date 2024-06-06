# 📦 Elementix

A web microframework built on Kotlin to build web applications using Kotlin. Developed from scratch without the use of
third-party libraries

## 🎯 Goal

This project has several goals:

1. Investigate the operation of modern web libraries for creating HTML-based client applications
2. Create and show what a library written in Kotlin for Kotlin could look like to create such applications
3. To present as my graduate work in a higher educational institution

## 📝 Proof of concept

At the moment, there is a proof of concept that demonstrates the operation of the interface, which has a counter and a
dynamic list, which has the same number of elements as a counter

[Code](elementix_test/src/jsMain/kotlin/Main.kt):

```kt
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
        props.id = count.map(Any::toString).reactiveProp // `ReadSignal#reactiveProp` converts `ReadSignal` to `ReactiveProp`
    }
    button {
        +clickText // We add the reactive text as a text node to the element
        props.onClick = { e: MouseEvent ->
            if (!e.shiftKey) {
                count { it + 1 }
            } else {
                count { it - 1 }
            }
        }.staticProp //`Any#staticProp` Turns anything into a Static Prop
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
        viewFor(list) { _, _ -> // `viewFor` generates a list of elements based on a list using a generator function
            li {
                +count.map { "#".repeat(it)  }
            }
        }
    }
}
```

## 🧰 Project modules

- [elementix_dom](./elementix_dom) - Virtual DOM
- [elementix_dom_ksp_processor](./elementix_dom_ksp_processor) - KSP Processor for [elementix_dom](./elementix_dom)
- [elementix_reactivity](./elementix_reactivity) - System of fine-grained reactivity
- [elementix_test](./elementix_test) - Test module
- [elementix_trpc](./elementix_trpc) - Type-safe Remote Procedure Call system

## 👟 How to launch

Clone and run the project. Call the `gradlew elementix_dom:jsRun` command and wait for the build to complete. After that, your
browser will open with a test page


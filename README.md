# 📦 Elementix

A web microframework built on Kotlin to build web applications using Kotlin. Developed from scratch without the use of
third-party libraries

## 🎯 Goal

This project has several goals:

1. Investigate the operation of modern web libraries for creating HTML-based client applications
2. Create and show what a library written in Kotlin for Kotlin could look like to create such applications
3. To present as my thesis in a higher educational institution

## 📝 Proof of concept

At the moment, there is a proof of concept that demonstrates the operation of the interface, which has a counter and a
dynamic list, which has the same number of elements as a counter

[Code](elementix_dom/src/jsMain/kotlin/Main.kt):

```kt
val count = Context.createSignal(0) // Signal
val list: ReadSignal<List<Int>> = ReadSignal { // Derive signal
    List(count()) { it } // Create a list of numbers whose length is the value of the counter
}
val clickText = ReadSignal { // Derive signal
    "CLICKED: ${count()}"
}

val root: Container = Root() // Root component
root.div { // Add div to root component
    div {
        /* Sets the value of the prop id of the counter value. 
        Due to the fact that we cannot directly set the String 
        variable to an Int type value, we convert it to the desired 
        type using a mapper */
        props.id = count.toProp(Any::toString) // `ReadSignal#toProp()` converts `ReadSignal` to `ReactiveProp`
    }
    button {
        +clickText
        props.onClick = { e: MouseEvent ->
            if (!e.shiftKey) {
                count(count() + 1)
            } else {
                count(count() - 1)
            }
        }.asProp() //Turns anything into a Static Prop
    }
    div {
        +"TEXT DIV  " // Adds a text node
        br // Br tag
        +clickText // Adds a reactive text node that updates values when the signal changes
        +"  END TEXT"
    }
    h1 {
        +"Dynamic FOR"
    }
    viewFor(list) { element, index -> // `viewFor` generates a list of elements based on a list using a generator function
        div {
            span {
                +element.toString()
            }
            div {
                +"Element index "
                +index.toString()
            }
        }
    }
}

root.render(document.getElementById("root")!!) // Display this virtual DOM tree in some element
```

## 🧰 Project modules

- [elementix_dom](./elementix_dom) - Virtual DOM
- [elementix_reactivity](./elementix_reactivity) - System of fine-grained reactivity

## 👟 How to launch

Clone and run the project. Call the `gradlew elementix_dom:jsRun` command and wait for the build to complete. After that, your
browser will open with a test page


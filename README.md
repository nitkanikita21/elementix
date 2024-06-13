# 📦 Elementix

A web micro-framework built on Kotlin for building web applications using Kotlin. Developed with minimal external
libraries

## 🎯 Goal

The goal of this project is to develop a set of systems for developing web applications using the Kotlin programming
language. This includes systems for reactivity, interface construction, and TRPCs.

## ✨ Features

### Common

#### Reactivity

The implementation of reactive primitives, such as Signal, Memo, Effect, and Trigger, is essential for creating reactive
code. These primitives play distinct roles in a reactive programming paradigm, enabling efficient and responsive data
handling within applications.

#### TRPC (Type-safe Remote Procedure Call)

A system that allows you to describe a single type-safe template of "procedures" for the client and the server. Thus,
you will be sure that your network code of client interaction with the server is 100% valid. There is support for
information validation using [konform](https://www.konform.io/).

### Frontend

##### Components DSL

Reactive interface description system. Allows you to develop a reactive interface based on html-like syntax. Here is a
list of possibilities:
* Description of the dom tree with html tags with the ability to define both static and reactive attributes
* Utilitarian components `viewFor` for dynamically generating content and `viewShow` for dynamically hiding content
* Definition of own components with the possibility of using any number of "slots" with content

### This code demonstrates the functionality available
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
        props.id =
            count.map(Any::toString).reactiveProp // `ReadSignal#reactiveProp` converts `ReadSignal` to `ReactiveProp`
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
                +count.map { "#".repeat(it) }
            }
        }
    }
}
```

## 🧰 Project modules

- [elementix_dom](./elementix_dom) - Virtual DOM
- [elementix_reactivity](./elementix_reactivity) - System of fine-grained reactivity
- [elementix_trpc](./elementix_trpc) - Type-safe Remote Procedure Call system
- [elementix_utils](./elementix_utils) - Utils module
- [elementix_test](./elementix_test) - Test module

## 👟 How to launch

Clone and run the project. Call the `gradlew elementix_dom:jsRun` command and wait for the build to complete. After
that, your
browser will open with a test page


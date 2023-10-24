import elementix.reactivity.Context
import elementix.reactivity.primitives.ReadSignal

//val cx = Context()

fun main() {
    val cx = Context()

    var count = cx.createSignal(0)
    val list = ReadSignal {
        mutableListOf<Int>().apply {
            console.log("each")
            for (i in 0..count.get()) {
                add(count.get())
            }
        }
    }


}
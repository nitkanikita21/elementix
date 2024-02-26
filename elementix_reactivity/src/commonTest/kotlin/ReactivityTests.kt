import elementix.reactivity.Context
import elementix.reactivity.primitives.Memo
import kotlin.test.Test
import kotlin.test.assertEquals

class ReactivityTests {
    @Test
    fun testSignalAndEffects() {
        val cx = Context

        val count = cx.createSignal(0)
        var paragraph = "not reactive yet"
        val increment = {
            count(count() + 1)
        }
        val decrement = {
            count(count() - 1)
        }

        val printElements = {
            println(paragraph)
        }

        cx.createEffect {
            paragraph = count().toString()
        }

        assertEquals("0", paragraph)
        printElements()

        increment()

        assertEquals("1", paragraph)
        printElements()

        increment()
        increment()
        increment()

        assertEquals("4", paragraph)
        printElements()
        decrement()
        decrement()

        assertEquals("2", paragraph)
        printElements()

        decrement()
        decrement()

        assertEquals("0", paragraph)
        printElements()
    }

    @Test
    fun testDerivedSignals() {
        val cx = Context

        val count = cx.createSignal(0)
        val doubleCount = {
            count.get() * 2
        }
        var paragraph = "not reactive yet"
        var doubleParagraph = "not reactive yet"
        val increment = {
            count(count() + 1)
        }
        val decrement = {
            count(count() - 1)
        }

        val printElements = {
            println("paragraph: $paragraph")
            println("doubleParagraph $doubleParagraph")
        }

        cx.createEffect {
            paragraph = count.get().toString()
        }

        cx.createEffect {
            doubleParagraph = doubleCount().toString()
        }

        assertEquals("0", paragraph)
        assertEquals("0", doubleParagraph)
        printElements()

        increment()

        assertEquals("1", paragraph)
        assertEquals("2", doubleParagraph)
        printElements()

        increment()
        increment()
        increment()

        assertEquals("4", paragraph)
        assertEquals("8", doubleParagraph)
        printElements()
        decrement()
        decrement()

        assertEquals("2", paragraph)
        assertEquals("4", doubleParagraph)
        printElements()

        decrement()
        decrement()

        assertEquals("0", paragraph)
        assertEquals("0", doubleParagraph)
        printElements()
    }

    @Test
    fun testFlowControl() {
        val cx = Context

        val showFullName = cx.createSignal(true)
        val firstName = cx.createSignal("Nitka")
        val lastName = cx.createSignal("Nikita")

        var notifications = 0

        var paragraph = "not reactive yet"

        cx.createEffect {
            paragraph = if (showFullName()) {
                "${firstName()} ${lastName()}"
            } else {
                firstName()
            }
            notifications++
        }

        assertEquals("Nitka Nikita", paragraph)
        assertEquals(1, notifications)

        firstName("Nitka1")
        assertEquals("Nitka1 Nikita", paragraph)
        assertEquals(2, notifications)

        lastName("Nikita1")
        assertEquals("Nitka1 Nikita1", paragraph)
        assertEquals(3, notifications)

        showFullName(false)
        assertEquals("Nitka1", paragraph)
        assertEquals(4, notifications)

        firstName("Nitka")
        assertEquals("Nitka", paragraph)
        assertEquals(5, notifications)

        lastName("Nikita")
        assertEquals("Nitka", paragraph)
        assertEquals(5, notifications)
    }

    @Test
    fun testMemos() {
        val cx = Context

        val width = cx.createSignal(5)
        val height = cx.createSignal(10)
        val area: Memo<Int> = cx.createMemo {
            width.get() * height.get()
        }

        var notifications = 0
        cx.createEffect {
            println(area())
            notifications++
        }

        assertEquals(50, area())
        assertEquals(1, notifications)

        width(5)
        assertEquals(50, area())
        assertEquals(1, notifications)

        height(10)
        assertEquals(50, area())
        assertEquals(1, notifications)

        width(3)
        assertEquals(30, area())
        assertEquals(2, notifications)

        height(5)
        assertEquals(15, area())
        assertEquals(3, notifications)

        height(5)
        assertEquals(15, area())
        assertEquals(3, notifications)
    }

    @Test
    fun testTrigger() {
        val cx = Context
        var notifyCount = 0

        val trigger = cx.createTrigger()

        cx.createEffect {
            trigger.subscribe()
            notifyCount++
        }

        assertEquals(1, notifyCount)

        trigger.fire()
        assertEquals(2, notifyCount)

        for (i in 0..<5) {
            trigger.fire()
        }
        assertEquals(7, notifyCount)
    }
}
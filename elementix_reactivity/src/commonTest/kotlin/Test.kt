import elementix.reactivity.Context
import elementix.reactivity.Memo
import elementix.reactivity.Signal
import kotlin.test.Test
import kotlin.test.assertEquals

class Test {
    class Button {
        var onClick: () -> Unit = {}
        var text: String = "Button"
    }

    class Paragraph {
        var text: String = "Button"
    }


    @Test
    fun testSignalAndEffects() {
        val signal = Context.createSignal(0)

        val buttonPlus = Button().apply {
            text = "+ 1"
            onClick = {
                signal.set(signal.get() + 1)
            }
        }
        val buttonMinus = Button().apply {
            text = "- 1"
            onClick = {
                signal.set(signal.get() - 1)
            }
        }
        val paragraph = Paragraph().apply {
            text = "not reactive yet"
        }

        val printElements = {
            println(paragraph.text)
        }

        Context.createEffect {
            paragraph.text = signal.get().toString()
        }

        assertEquals("0", paragraph.text)
        printElements()

        buttonPlus.onClick()

        assertEquals("1", paragraph.text)
        printElements()

        buttonPlus.onClick()
        buttonPlus.onClick()
        buttonPlus.onClick()

        assertEquals("4", paragraph.text)
        printElements()
        buttonMinus.onClick()
        buttonMinus.onClick()

        assertEquals("2", paragraph.text)
        printElements()

        buttonMinus.onClick()
        buttonMinus.onClick()

        assertEquals("0", paragraph.text)
        printElements()
    }

    @Test
    fun testDerivedSignals() {
        val count = Context.createSignal(0)
        val double = {
            count.get() * 2
        }

        val buttonPlus = Button().apply {
            text = "+ 1"
            onClick = {
                count.set(count.get() + 1)
            }
        }
        val buttonMinus = Button().apply {
            text = "- 1"
            onClick = {
                count.set(count.get() - 1)
            }
        }
        val paragraph = Paragraph().apply {
            text = "not reactive yet"
        }

        val paragraphDouble = Paragraph().apply {
            text = "not reactive yet"
        }

        val printElements = {
            println(paragraph.text)
            println("doubleParagraph ${paragraphDouble.text}")
        }

        Context.createEffect {
            paragraph.text = count.get().toString()
        }

        Context.createEffect {
            paragraphDouble.text = double().toString()
        }

        assertEquals("0", paragraph.text)
        assertEquals("0", paragraphDouble.text)
        printElements()

        buttonPlus.onClick()

        assertEquals("1", paragraph.text)
        assertEquals("2", paragraphDouble.text)
        printElements()

        buttonPlus.onClick()
        buttonPlus.onClick()
        buttonPlus.onClick()

        assertEquals("4", paragraph.text)
        assertEquals("8", paragraphDouble.text)
        printElements()
        buttonMinus.onClick()
        buttonMinus.onClick()

        assertEquals("2", paragraph.text)
        assertEquals("4", paragraphDouble.text)
        printElements()

        buttonMinus.onClick()
        buttonMinus.onClick()

        assertEquals("0", paragraph.text)
        assertEquals("0", paragraphDouble.text)
        printElements()
    }

    @Test
    fun testFlowControl() {
        val showFullName = Context.createSignal(true)
        val firstName = Context.createSignal("Nitka")
        val lastName = Context.createSignal("Nikita")

        var renderedFull = 0
        var renderedFirst = 0

        val paragraph = Paragraph().apply {
            text = "not reactive yet"
        }

        Context.createEffect {
            if (showFullName.get()) {
                paragraph.text = "${firstName.get()} ${lastName.get()}"
                renderedFull++
            } else {
                paragraph.text = firstName.get()
                renderedFirst++
            }
        }

        assertEquals("Nitka Nikita", paragraph.text)
        assertEquals(1, renderedFull)
        assertEquals(0, renderedFirst)
        showFullName.set(false)
        assertEquals("Nitka", paragraph.text)
        assertEquals(1, renderedFull)
        assertEquals(1, renderedFirst)
        showFullName.set(true)
        assertEquals("Nitka Nikita", paragraph.text)
        assertEquals(2, renderedFull)
        assertEquals(1, renderedFirst)
    }

    @Test
    fun testMemos() {
        val width = Context.createSignal(5)
        val height = Context.createSignal(10)
        val area: Memo<Int> = Context.createMemo {
            width.get() * height.get()
        }

        var notifications = 0
        Context.createEffect {
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
}
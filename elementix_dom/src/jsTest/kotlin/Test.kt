import elementix.dom.FC
import elementix.dom.injectFC
import kotlinx.browser.document
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.dom.create
import kotlinx.html.html
import kotlinx.html.stream.appendHTML
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLParagraphElement
import kotlin.properties.Delegates
import kotlin.test.Test

class Test {
    @Test
    fun test() {
        val testFC = FC<String> { props ->
            div {
                +props
            }
        }

        val text = buildString {
            appendLine("<!DOCTYPE html>")

            appendHTML().html {
                body {

                    this injectFC testFC("world")
                    this injectFC testFC("w432424orld")

                    +"sample text"
                    div {
                        +"in another div"
                        this injectFC testFC("wdfsfsfdsorld")
                        this injectFC testFC("w434orld")
                    }
                }
            }


            appendLine()
        }

        println(text)
    }
}
package elementix.dom.tags

import elementix.dom.view.ElementDescriptor
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement

fun div(): ElementDescriptor<HTMLDivElement> {
    return ElementDescriptor(document.createElement("div") as HTMLDivElement)
}


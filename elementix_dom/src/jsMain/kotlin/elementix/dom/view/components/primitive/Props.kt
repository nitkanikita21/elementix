package elementix.dom.view.components.primitive

import elementix.dom.view.View
import elementix.reactivity.Context
import elementix.reactivity.primitives.ReadSignal
import org.w3c.dom.*
import org.w3c.dom.clipboard.ClipboardEvent
import org.w3c.dom.css.CSSStyleDeclaration
import org.w3c.dom.events.*
import org.w3c.dom.pointerevents.PointerEvent
import org.w3c.xhr.ProgressEvent

internal interface EffectCreator {
    fun initEffects(cx: Context)
}

interface ClassnameAttributes {
    var className: Attribute<String>

    fun className(vararg classes: String) {
        className = Attribute(classes.joinToString(" "))
    }
}

open class DefaultElementAttributes<E : Element>(
    protected val element: E,
) : EffectCreator, Attributes, ClassnameAttributes {
    override var children: Attribute<List<View>> = Attribute(listOf<View>())

    //<editor-fold desc="properties">
    open var id: Attribute<String> = Attribute(element.id)
    override var className: Attribute<String> = Attribute(element.className)
    open var slot: Attribute<String> = Attribute(element.slot)
    open var scrollTop: Attribute<Double> = Attribute(element.scrollTop)
    open var scrollLeft: Attribute<Double> = Attribute(element.scrollLeft)
    open var innerHTML: Attribute<String> = Attribute(element.innerHTML)
//    open var outerHTML: Prop<String> = StaticProp(element.outerHTML)
    //</editor-fold>

    //<editor-fold desc="read-only properties">
    open val namespaceURI: Attribute<String?> = Attribute(element.namespaceURI)
    open val prefix: Attribute<String?> = Attribute(element.prefix)
    open val localName: Attribute<String> = Attribute(element.localName)
    open val tagName: Attribute<String> = Attribute(element.tagName)
    open val classList: Attribute<DOMTokenList> = Attribute(element.classList)
    open val attributes: Attribute<NamedNodeMap> = Attribute(element.attributes)
    open val shadowRoot: Attribute<ShadowRoot?> = Attribute(element.shadowRoot)
    open val scrollWidth: Attribute<Int> = Attribute(element.scrollWidth)
    open val scrollHeight: Attribute<Int> = Attribute(element.scrollHeight)
    open val clientTop: Attribute<Int> = Attribute(element.clientTop)
    open val clientLeft: Attribute<Int> = Attribute(element.clientLeft)
    open val clientWidth: Attribute<Int> = Attribute(element.clientWidth)
    open val clientHeight: Attribute<Int> = Attribute(element.clientHeight)
    //</editor-fold>

    override fun initEffects(cx: Context) {
        cx.createEffect { id.ifNotEquals(element.id) { element.id = it } }
        cx.createEffect { className.ifNotEquals(element.className) { element.className = it } }
        cx.createEffect { slot.ifNotEquals(element.slot) { element.slot = it } }
        cx.createEffect { scrollTop.ifNotEquals(element.scrollTop) { element.scrollTop = it } }
        cx.createEffect { scrollLeft.ifNotEquals(element.scrollLeft) { element.scrollLeft = it } }
        cx.createEffect { innerHTML.ifNotEquals(element.innerHTML) { element.innerHTML = it } }
//        cx.createEffect { outerHTML.ifNotEquals(element.outerHTML) { element.outerHTML = it } }
    }
}

open class DefaultHTMLElementAttributes<E : HTMLElement>(
    element: E
) : DefaultElementAttributes<E>(element) {
    private val dataAttributes: MutableMap<String, ReadSignal<String>> = mutableMapOf()


    fun data(key: String, value: String) {
        dataAttributes.remove(key)
        element.dataset[key] = value
    }

    fun data(key: String, value: ReadSignal<String>) {
        dataAttributes[key] = value
    }

    //<editor-fold desc="properties">
    open var title: Attribute<String> = Attribute(element.title)
    open var lang: Attribute<String> = Attribute(element.lang)
    open var translate: Attribute<Boolean> = Attribute(element.translate)
    open var dir: Attribute<String> = Attribute(element.dir)
    open var hidden: Attribute<Boolean> = Attribute(element.hidden)
    open var tabIndex: Attribute<Int> = Attribute(element.tabIndex)
    open var accessKey: Attribute<String> = Attribute(element.accessKey)
    open var draggable: Attribute<Boolean> = Attribute(element.draggable)
    open var contextMenu: Attribute<HTMLMenuElement?> = Attribute(element.contextMenu)
    open var spellcheck: Attribute<Boolean> = Attribute(element.spellcheck)
    open var innerText: Attribute<String> = Attribute(element.innerText)
    open var contentEditable: Attribute<String> = Attribute(element.contentEditable)
    //</editor-fold>

    //<editor-fold desc="read-only properties">
    open val style: Attribute<CSSStyleDeclaration> = Attribute(element.style)
    open val dataset: Attribute<DOMStringMap> = Attribute(element.dataset)
    open val accessKeyLabel: Attribute<String> = Attribute(element.accessKeyLabel)
    open val dropzone: Attribute<DOMTokenList> = Attribute(element.dropzone)
    open val offsetParent: Attribute<Element?> = Attribute(element.offsetParent)
    open val offsetTop: Attribute<Int> = Attribute(element.offsetTop)
    open val offsetLeft: Attribute<Int> = Attribute(element.offsetLeft)
    open val offsetWidth: Attribute<Int> = Attribute(element.offsetWidth)
    open val offsetHeight: Attribute<Int> = Attribute(element.offsetHeight)
    open val isContentEditable: Attribute<Boolean> = Attribute(element.isContentEditable)
    //</editor-fold>

    //<editor-fold desc="event handlers" defaultstate="collapsed">
    open var onAbort: Attribute<((Event) -> dynamic)?> = Attribute(element.onabort)
    open var onBlur: Attribute<((FocusEvent) -> dynamic)?> = Attribute(element.onblur)
    open var onCancel: Attribute<((Event) -> dynamic)?> = Attribute(element.oncancel)
    open var onCanPlay: Attribute<((Event) -> dynamic)?> = Attribute(element.oncanplay)
    open var onCanPlayThrough: Attribute<((Event) -> dynamic)?> = Attribute(element.oncanplaythrough)
    open var onChange: Attribute<((Event) -> dynamic)?> = Attribute(element.onchange)
    open var onClick: Attribute<((MouseEvent) -> dynamic)?> = Attribute(element.onclick)
    open var onClose: Attribute<((Event) -> dynamic)?> = Attribute(element.onclose)
    open var onContextMenu: Attribute<((MouseEvent) -> dynamic)?> = Attribute(element.oncontextmenu)
    open var onCueChange: Attribute<((Event) -> dynamic)?> = Attribute(element.oncuechange)
    open var onDblClick: Attribute<((MouseEvent) -> dynamic)?> = Attribute(element.ondblclick)
    open var onDrag: Attribute<((DragEvent) -> dynamic)?> = Attribute(element.ondrag)
    open var onDragEnd: Attribute<((DragEvent) -> dynamic)?> = Attribute(element.ondragend)
    open var onDragEnter: Attribute<((DragEvent) -> dynamic)?> = Attribute(element.ondragenter)
    open var onDragExit: Attribute<((DragEvent) -> dynamic)?> = Attribute(element.ondragexit)
    open var onDragLeave: Attribute<((DragEvent) -> dynamic)?> = Attribute(element.ondragleave)
    open var onDragOver: Attribute<((DragEvent) -> dynamic)?> = Attribute(element.ondragover)
    open var onDragStart: Attribute<((DragEvent) -> dynamic)?> = Attribute(element.ondragstart)
    open var onDrop: Attribute<((DragEvent) -> dynamic)?> = Attribute(element.ondrop)
    open var onDurationChange: Attribute<((Event) -> dynamic)?> = Attribute(element.ondurationchange)
    open var onEmptied: Attribute<((Event) -> dynamic)?> = Attribute(element.onemptied)
    open var onEnded: Attribute<((Event) -> dynamic)?> = Attribute(element.onended)
    open var onError: Attribute<((dynamic, String, Int, Int, Any?) -> dynamic)?> = Attribute(element.onerror)
    open var onFocus: Attribute<((FocusEvent) -> dynamic)?> = Attribute(element.onfocus)
    open var onInput: Attribute<((InputEvent) -> dynamic)?> = Attribute(element.oninput)
    open var onInvalid: Attribute<((Event) -> dynamic)?> = Attribute(element.oninvalid)
    open var onKeydown: Attribute<((KeyboardEvent) -> dynamic)?> = Attribute(element.onkeydown)
    open var onKeypress: Attribute<((KeyboardEvent) -> dynamic)?> = Attribute(element.onkeypress)
    open var onKeyup: Attribute<((KeyboardEvent) -> dynamic)?> = Attribute(element.onkeyup)
    open var onLoad: Attribute<((Event) -> dynamic)?> = Attribute(element.onload)
    open var onLoadedData: Attribute<((Event) -> dynamic)?> = Attribute(element.onloadeddata)
    open var onLoadedMetadata: Attribute<((Event) -> dynamic)?> = Attribute(element.onloadedmetadata)
    open var onLoadEnd: Attribute<((Event) -> dynamic)?> = Attribute(element.onloadend)
    open var onLoadStart: Attribute<((ProgressEvent) -> dynamic)?> = Attribute(element.onloadstart)
    open var onMouseDown: Attribute<((MouseEvent) -> dynamic)?> = Attribute(element.onmousedown)
    open var onMouseEnter: Attribute<((MouseEvent) -> dynamic)?> = Attribute(element.onmouseenter)
    open var onMouseLeave: Attribute<((MouseEvent) -> dynamic)?> = Attribute(element.onmouseleave)
    open var onMouseMove: Attribute<((MouseEvent) -> dynamic)?> = Attribute(element.onmousemove)
    open var onMouseOut: Attribute<((MouseEvent) -> dynamic)?> = Attribute(element.onmouseout)
    open var onMouseOver: Attribute<((MouseEvent) -> dynamic)?> = Attribute(element.onmouseover)
    open var onMouseUp: Attribute<((MouseEvent) -> dynamic)?> = Attribute(element.onmouseup)
    open var onWheel: Attribute<((WheelEvent) -> dynamic)?> = Attribute(element.onwheel)
    open var onPause: Attribute<((Event) -> dynamic)?> = Attribute(element.onpause)
    open var onPlay: Attribute<((Event) -> dynamic)?> = Attribute(element.onplay)
    open var onPlaying: Attribute<((Event) -> dynamic)?> = Attribute(element.onplaying)
    open var onProgress: Attribute<((ProgressEvent) -> dynamic)?> = Attribute(element.onprogress)
    open var onRateChange: Attribute<((Event) -> dynamic)?> = Attribute(element.onratechange)
    open var onReset: Attribute<((Event) -> dynamic)?> = Attribute(element.onreset)
    open var onResize: Attribute<((Event) -> dynamic)?> = Attribute(element.onresize)
    open var onScroll: Attribute<((Event) -> dynamic)?> = Attribute(element.onscroll)
    open var onSeeked: Attribute<((Event) -> dynamic)?> = Attribute(element.onseeked)
    open var onSeeking: Attribute<((Event) -> dynamic)?> = Attribute(element.onseeking)
    open var onSelect: Attribute<((Event) -> dynamic)?> = Attribute(element.onselect)
    open var onShow: Attribute<((Event) -> dynamic)?> = Attribute(element.onshow)
    open var onStalled: Attribute<((Event) -> dynamic)?> = Attribute(element.onstalled)
    open var onSubmit: Attribute<((Event) -> dynamic)?> = Attribute(element.onsubmit)
    open var onSuspend: Attribute<((Event) -> dynamic)?> = Attribute(element.onsuspend)
    open var onTimeUpdate: Attribute<((Event) -> dynamic)?> = Attribute(element.ontimeupdate)
    open var onToggle: Attribute<((Event) -> dynamic)?> = Attribute(element.ontoggle)
    open var onVolumeChange: Attribute<((Event) -> dynamic)?> = Attribute(element.onvolumechange)
    open var onWaiting: Attribute<((Event) -> dynamic)?> = Attribute(element.onwaiting)
    open var onGotPointerCapture: Attribute<((PointerEvent) -> dynamic)?> = Attribute(element.ongotpointercapture)
    open var onLostPointerCapture: Attribute<((PointerEvent) -> dynamic)?> = Attribute(element.onlostpointercapture)
    open var onPointerDown: Attribute<((PointerEvent) -> dynamic)?> = Attribute(element.onpointerdown)
    open var onPointerMove: Attribute<((PointerEvent) -> dynamic)?> = Attribute(element.onpointermove)
    open var onPointerUp: Attribute<((PointerEvent) -> dynamic)?> = Attribute(element.onpointerup)
    open var onPointerCancel: Attribute<((PointerEvent) -> dynamic)?> = Attribute(element.onpointercancel)
    open var onPointerOver: Attribute<((PointerEvent) -> dynamic)?> = Attribute(element.onpointerover)
    open var onPointerOut: Attribute<((PointerEvent) -> dynamic)?> = Attribute(element.onpointerout)
    open var onPointerEnter: Attribute<((PointerEvent) -> dynamic)?> = Attribute(element.onpointerenter)
    open var onPointerLeave: Attribute<((PointerEvent) -> dynamic)?> = Attribute(element.onpointerleave)

    open var onCopy: Attribute<((ClipboardEvent) -> dynamic)?> = Attribute(element.oncopy)
    open var onCut: Attribute<((ClipboardEvent) -> dynamic)?> = Attribute(element.oncut)
    open var onPaste: Attribute<((ClipboardEvent) -> dynamic)?> = Attribute(element.onpaste)
    //</editor-fold>

    override fun initEffects(cx: Context) {
        super.initEffects(cx)

        cx.createEffect { title.ifNotEquals(element.title) { element.title = it } }
        cx.createEffect { lang.ifNotEquals(element.lang) { element.lang = it } }
        cx.createEffect { translate.ifNotEquals(element.translate) { element.translate = it } }
        cx.createEffect { dir.ifNotEquals(element.dir) { element.dir = it } }
        cx.createEffect { hidden.ifNotEquals(element.hidden) { element.hidden = it } }
        cx.createEffect { tabIndex.ifNotEquals(element.tabIndex) { element.tabIndex = it } }
        cx.createEffect { accessKey.ifNotEquals(element.accessKey) { element.accessKey = it } }
        cx.createEffect { draggable.ifNotEquals(element.draggable) { element.draggable = it } }
        cx.createEffect { contextMenu.ifNotEquals(element.contextMenu) { element.contextMenu = it } }
        cx.createEffect { spellcheck.ifNotEquals(element.spellcheck) { element.spellcheck = it } }
        cx.createEffect { innerText.ifNotEquals(element.innerText) { element.innerText = it } }
        cx.createEffect { contentEditable.ifNotEquals(element.contentEditable) { element.contentEditable = it } }

        cx.createEffect { onAbort.ifNotEquals(element.onabort) { element.onabort = it } }
        cx.createEffect { onBlur.ifNotEquals(element.onblur) { element.onblur = it } }
        cx.createEffect { onCancel.ifNotEquals(element.oncancel) { element.oncancel = it } }
        cx.createEffect { onCanPlay.ifNotEquals(element.oncanplay) { element.oncanplay = it } }
        cx.createEffect { onCanPlayThrough.ifNotEquals(element.oncanplaythrough) { element.oncanplaythrough = it } }
        cx.createEffect { onChange.ifNotEquals(element.onchange) { element.onchange = it } }
        cx.createEffect { onClick.ifNotEquals(element.onclick) { element.onclick = it } }
        cx.createEffect { onClose.ifNotEquals(element.onclose) { element.onclose = it } }
        cx.createEffect { onContextMenu.ifNotEquals(element.oncontextmenu) { element.oncontextmenu = it } }
        cx.createEffect { onCueChange.ifNotEquals(element.oncuechange) { element.oncuechange = it } }
        cx.createEffect { onDblClick.ifNotEquals(element.ondblclick) { element.ondblclick = it } }
        cx.createEffect { onDrag.ifNotEquals(element.ondrag) { element.ondrag = it } }
        cx.createEffect { onDragEnd.ifNotEquals(element.ondragend) { element.ondragend = it } }
        cx.createEffect { onDragEnter.ifNotEquals(element.ondragenter) { element.ondragenter = it } }
        cx.createEffect { onDragExit.ifNotEquals(element.ondragexit) { element.ondragexit = it } }
        cx.createEffect { onDragLeave.ifNotEquals(element.ondragleave) { element.ondragleave = it } }
        cx.createEffect { onDragOver.ifNotEquals(element.ondragover) { element.ondragover = it } }
        cx.createEffect { onDragStart.ifNotEquals(element.ondragstart) { element.ondragstart = it } }
        cx.createEffect { onDrop.ifNotEquals(element.ondrop) { element.ondrop = it } }
        cx.createEffect { onDurationChange.ifNotEquals(element.ondurationchange) { element.ondurationchange = it } }
        cx.createEffect { onEmptied.ifNotEquals(element.onemptied) { element.onemptied = it } }
        cx.createEffect { onEnded.ifNotEquals(element.onended) { element.onended = it } }
        cx.createEffect { onError.ifNotEquals(element.onerror) { element.onerror = it } }
        cx.createEffect { onFocus.ifNotEquals(element.onfocus) { element.onfocus = it } }
        cx.createEffect { onInput.ifNotEquals(element.oninput) { element.oninput = it } }
        cx.createEffect { onInvalid.ifNotEquals(element.oninvalid) { element.oninvalid = it } }
        cx.createEffect { onKeydown.ifNotEquals(element.onkeydown) { element.onkeydown = it } }
        cx.createEffect { onKeypress.ifNotEquals(element.onkeypress) { element.onkeypress = it } }
        cx.createEffect { onKeyup.ifNotEquals(element.onkeyup) { element.onkeyup = it } }
        cx.createEffect { onLoad.ifNotEquals(element.onload) { element.onload = it } }
        cx.createEffect { onLoadedData.ifNotEquals(element.onloadeddata) { element.onloadeddata = it } }
        cx.createEffect { onLoadedMetadata.ifNotEquals(element.onloadedmetadata) { element.onloadedmetadata = it } }
        cx.createEffect { onLoadEnd.ifNotEquals(element.onloadend) { element.onloadend = it } }
        cx.createEffect { onLoadStart.ifNotEquals(element.onloadstart) { element.onloadstart = it } }
        cx.createEffect { onMouseDown.ifNotEquals(element.onmousedown) { element.onmousedown = it } }
        cx.createEffect { onMouseEnter.ifNotEquals(element.onmouseenter) { element.onmouseenter = it } }
        cx.createEffect { onMouseLeave.ifNotEquals(element.onmouseleave) { element.onmouseleave = it } }
        cx.createEffect { onMouseMove.ifNotEquals(element.onmousemove) { element.onmousemove = it } }
        cx.createEffect { onMouseOut.ifNotEquals(element.onmouseout) { element.onmouseout = it } }
        cx.createEffect { onMouseOver.ifNotEquals(element.onmouseover) { element.onmouseover = it } }
        cx.createEffect { onMouseUp.ifNotEquals(element.onmouseup) { element.onmouseup = it } }
        cx.createEffect { onWheel.ifNotEquals(element.onwheel) { element.onwheel = it } }

        cx.createEffect { onPause.ifNotEquals(element.onpause) { element.onpause = it } }
        cx.createEffect { onPlay.ifNotEquals(element.onplay) { element.onplay = it } }
        cx.createEffect { onPlaying.ifNotEquals(element.onplaying) { element.onplaying = it } }
        cx.createEffect { onProgress.ifNotEquals(element.onprogress) { element.onprogress = it } }
        cx.createEffect { onRateChange.ifNotEquals(element.onratechange) { element.onratechange = it } }
        cx.createEffect { onReset.ifNotEquals(element.onreset) { element.onreset = it } }
        cx.createEffect { onResize.ifNotEquals(element.onresize) { element.onresize = it } }
        cx.createEffect { onScroll.ifNotEquals(element.onscroll) { element.onscroll = it } }
        cx.createEffect { onSeeked.ifNotEquals(element.onseeked) { element.onseeked = it } }
        cx.createEffect { onSeeking.ifNotEquals(element.onseeking) { element.onseeking = it } }
        cx.createEffect { onSelect.ifNotEquals(element.onselect) { element.onselect = it } }
        cx.createEffect { onShow.ifNotEquals(element.onshow) { element.onshow = it } }
        cx.createEffect { onStalled.ifNotEquals(element.onstalled) { element.onstalled = it } }
        cx.createEffect { onSubmit.ifNotEquals(element.onsubmit) { element.onsubmit = it } }
        cx.createEffect { onSuspend.ifNotEquals(element.onsuspend) { element.onsuspend = it } }
        cx.createEffect { onTimeUpdate.ifNotEquals(element.ontimeupdate) { element.ontimeupdate = it } }
        cx.createEffect { onToggle.ifNotEquals(element.ontoggle) { element.ontoggle = it } }
        cx.createEffect { onVolumeChange.ifNotEquals(element.onvolumechange) { element.onvolumechange = it } }
        cx.createEffect { onWaiting.ifNotEquals(element.onwaiting) { element.onwaiting = it } }
        cx.createEffect {
            onGotPointerCapture.ifNotEquals(element.ongotpointercapture) {
                element.ongotpointercapture = it
            }
        }
        cx.createEffect {
            onLostPointerCapture.ifNotEquals(element.onlostpointercapture) {
                element.onlostpointercapture = it
            }
        }
        cx.createEffect { onPointerDown.ifNotEquals(element.onpointerdown) { element.onpointerdown = it } }
        cx.createEffect { onPointerMove.ifNotEquals(element.onpointermove) { element.onpointermove = it } }
        cx.createEffect { onPointerUp.ifNotEquals(element.onpointerup) { element.onpointerup = it } }
        cx.createEffect { onPointerCancel.ifNotEquals(element.onpointercancel) { element.onpointercancel = it } }
        cx.createEffect { onPointerOver.ifNotEquals(element.onpointerover) { element.onpointerover = it } }
        cx.createEffect { onPointerOut.ifNotEquals(element.onpointerout) { element.onpointerout = it } }
        cx.createEffect { onPointerEnter.ifNotEquals(element.onpointerenter) { element.onpointerenter = it } }
        cx.createEffect { onPointerLeave.ifNotEquals(element.onpointerleave) { element.onpointerleave = it } }
        cx.createEffect { onCopy.ifNotEquals(element.oncopy) { element.oncopy = it } }
        cx.createEffect { onCut.ifNotEquals(element.oncut) { element.oncut = it } }
        cx.createEffect { onPaste.ifNotEquals(element.onpaste) { element.onpaste = it } }

        dataAttributes.forEach { (key, signal) ->
            cx.createEffect {
                element.dataset[key] = signal.get()
            }
        }
    }
}

open class DefaultWindowEventHandlerAttributes<E : WindowEventHandlers>(
    protected val windowElement: E
) : DefaultHTMLElementAttributes<HTMLElement>(windowElement as HTMLElement) {
    open var onAfterPrint: Attribute<((Event) -> dynamic)?> = Attribute(windowElement.onafterprint)
    open var onBeforePrint: Attribute<((Event) -> dynamic)?> = Attribute(windowElement.onbeforeprint)
    open var onBeforeUnload: Attribute<((BeforeUnloadEvent) -> String?)?> = Attribute(windowElement.onbeforeunload)
    open var onHashChange: Attribute<((HashChangeEvent) -> dynamic)?> = Attribute(windowElement.onhashchange)
    open var onLanguageChange: Attribute<((Event) -> dynamic)?> = Attribute(windowElement.onlanguagechange)
    open var onMessage: Attribute<((MessageEvent) -> dynamic)?> = Attribute(windowElement.onmessage)
    open var onOffline: Attribute<((Event) -> dynamic)?> = Attribute(windowElement.onoffline)
    open var onOnline: Attribute<((Event) -> dynamic)?> = Attribute(windowElement.ononline)
    open var onPageHide: Attribute<((PageTransitionEvent) -> dynamic)?> = Attribute(windowElement.onpagehide)
    open var onPageShow: Attribute<((PageTransitionEvent) -> dynamic)?> = Attribute(windowElement.onpageshow)
    open var onPopState: Attribute<((PopStateEvent) -> dynamic)?> = Attribute(windowElement.onpopstate)
    open var onRejectionHandled: Attribute<((Event) -> dynamic)?> = Attribute(windowElement.onrejectionhandled)
    open var onStorage: Attribute<((StorageEvent) -> dynamic)?> = Attribute(windowElement.onstorage)
    open var onUnhandledRejection: Attribute<((PromiseRejectionEvent) -> dynamic)?> =
        Attribute(windowElement.onunhandledrejection)
    open var onUnload: Attribute<((Event) -> dynamic)?> = Attribute(windowElement.onunload)

    override fun initEffects(cx: Context) {
        super.initEffects(cx)

        cx.createEffect { onAfterPrint.ifNotEquals(windowElement.onafterprint) { windowElement.onafterprint = it } }
        cx.createEffect { onBeforePrint.ifNotEquals(windowElement.onbeforeprint) { windowElement.onbeforeprint = it } }
        cx.createEffect {
            onBeforeUnload.ifNotEquals(windowElement.onbeforeunload) {
                windowElement.onbeforeunload = it
            }
        }
        cx.createEffect { onHashChange.ifNotEquals(windowElement.onhashchange) { windowElement.onhashchange = it } }
        cx.createEffect {
            onLanguageChange.ifNotEquals(windowElement.onlanguagechange) {
                windowElement.onlanguagechange = it
            }
        }
        cx.createEffect { onMessage.ifNotEquals(windowElement.onmessage) { windowElement.onmessage = it } }
        cx.createEffect { onOffline.ifNotEquals(windowElement.onoffline) { windowElement.onoffline = it } }
        cx.createEffect { onOnline.ifNotEquals(windowElement.ononline) { windowElement.ononline = it } }
        cx.createEffect { onPageHide.ifNotEquals(windowElement.onpagehide) { windowElement.onpagehide = it } }
        cx.createEffect { onPageShow.ifNotEquals(windowElement.onpageshow) { windowElement.onpageshow = it } }
        cx.createEffect { onPopState.ifNotEquals(windowElement.onpopstate) { windowElement.onpopstate = it } }
        cx.createEffect {
            onRejectionHandled.ifNotEquals(windowElement.onrejectionhandled) {
                windowElement.onrejectionhandled = it
            }
        }
        cx.createEffect { onStorage.ifNotEquals(windowElement.onstorage) { windowElement.onstorage = it } }
        cx.createEffect {
            onUnhandledRejection.ifNotEquals(windowElement.onunhandledrejection) {
                windowElement.onunhandledrejection = it
            }
        }
        cx.createEffect { onUnload.ifNotEquals(windowElement.onunload) { windowElement.onunload = it } }
    }
}


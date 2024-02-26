package elementix.dom.tags

import elementix.dom.Prop
import elementix.dom.Props
import elementix.dom.StaticProp
import elementix.dom.asProp
import elementix.dom.view.Component
import elementix.dom.view.Container
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

open class DefaultElementProps<E : Element>(
    protected val element: E,
) : EffectCreator, Props {
    override var children: Prop<List<Container>> = listOf<Container>().asProp()

    //<editor-fold desc="properties">
    open var id: Prop<String> = StaticProp(element.id)
    open var className: Prop<String> = StaticProp(element.className)
    open var slot: Prop<String> = StaticProp(element.slot)
    open var scrollTop: Prop<Double> = StaticProp(element.scrollTop)
    open var scrollLeft: Prop<Double> = StaticProp(element.scrollLeft)
    open var innerHTML: Prop<String> = StaticProp(element.innerHTML)
//    open var outerHTML: Prop<String> = StaticProp(element.outerHTML)
    //</editor-fold>

    //<editor-fold desc="read-only properties">
    open val namespaceURI: Prop<String?> = StaticProp(element.namespaceURI)
    open val prefix: Prop<String?> = StaticProp(element.prefix)
    open val localName: Prop<String> = StaticProp(element.localName)
    open val tagName: Prop<String> = StaticProp(element.tagName)
    open val classList: Prop<DOMTokenList> = StaticProp(element.classList)
    open val attributes: Prop<NamedNodeMap> = StaticProp(element.attributes)
    open val shadowRoot: Prop<ShadowRoot?> = StaticProp(element.shadowRoot)
    open val scrollWidth: Prop<Int> = StaticProp(element.scrollWidth)
    open val scrollHeight: Prop<Int> = StaticProp(element.scrollHeight)
    open val clientTop: Prop<Int> = StaticProp(element.clientTop)
    open val clientLeft: Prop<Int> = StaticProp(element.clientLeft)
    open val clientWidth: Prop<Int> = StaticProp(element.clientWidth)
    open val clientHeight: Prop<Int> = StaticProp(element.clientHeight)
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

open class DefaultHTMLElementProps<E : HTMLElement>(
    element: E
) : DefaultElementProps<E>(element) {
    private val dataAttributes: MutableMap<String, ReadSignal<String>> = mutableMapOf()


    fun data(key: String, value: String) {
        dataAttributes.remove(key)
        element.dataset[key] = value
    }

    fun data(key: String, value: ReadSignal<String>) {
        dataAttributes.put(key, value)
    }

    //<editor-fold desc="properties">
    open var title: Prop<String> = StaticProp(element.title)
    open var lang: Prop<String> = StaticProp(element.lang)
    open var translate: Prop<Boolean> = StaticProp(element.translate)
    open var dir: Prop<String> = StaticProp(element.dir)
    open var hidden: Prop<Boolean> = StaticProp(element.hidden)
    open var tabIndex: Prop<Int> = StaticProp(element.tabIndex)
    open var accessKey: Prop<String> = StaticProp(element.accessKey)
    open var draggable: Prop<Boolean> = StaticProp(element.draggable)
    open var contextMenu: Prop<HTMLMenuElement?> = StaticProp(element.contextMenu)
    open var spellcheck: Prop<Boolean> = StaticProp(element.spellcheck)
    open var innerText: Prop<String> = StaticProp(element.innerText)
    open var contentEditable: Prop<String> = StaticProp(element.contentEditable)
    //</editor-fold>

    //<editor-fold desc="read-only properties">
    open val style: Prop<CSSStyleDeclaration> = StaticProp(element.style)
    open val dataset: Prop<DOMStringMap> = StaticProp(element.dataset)
    open val accessKeyLabel: Prop<String> = StaticProp(element.accessKeyLabel)
    open val dropzone: Prop<DOMTokenList> = StaticProp(element.dropzone)
    open val offsetParent: Prop<Element?> = StaticProp(element.offsetParent)
    open val offsetTop: Prop<Int> = StaticProp(element.offsetTop)
    open val offsetLeft: Prop<Int> = StaticProp(element.offsetLeft)
    open val offsetWidth: Prop<Int> = StaticProp(element.offsetWidth)
    open val offsetHeight: Prop<Int> = StaticProp(element.offsetHeight)
    open val isContentEditable: Prop<Boolean> = StaticProp(element.isContentEditable)
    //</editor-fold>

    //<editor-fold desc="event handlers" defaultstate="collapsed">
    open var onAbort: Prop<((Event) -> dynamic)?> = StaticProp(element.onabort)
    open var onBlur: Prop<((FocusEvent) -> dynamic)?> = StaticProp(element.onblur)
    open var onCancel: Prop<((Event) -> dynamic)?> = StaticProp(element.oncancel)
    open var onCanPlay: Prop<((Event) -> dynamic)?> = StaticProp(element.oncanplay)
    open var onCanPlayThrough: Prop<((Event) -> dynamic)?> = StaticProp(element.oncanplaythrough)
    open var onChange: Prop<((Event) -> dynamic)?> = StaticProp(element.onchange)
    open var onClick: Prop<((MouseEvent) -> dynamic)?> = StaticProp(element.onclick)
    open var onClose: Prop<((Event) -> dynamic)?> = StaticProp(element.onclose)
    open var onContextMenu: Prop<((MouseEvent) -> dynamic)?> = StaticProp(element.oncontextmenu)
    open var onCueChange: Prop<((Event) -> dynamic)?> = StaticProp(element.oncuechange)
    open var onDblClick: Prop<((MouseEvent) -> dynamic)?> = StaticProp(element.ondblclick)
    open var onDrag: Prop<((DragEvent) -> dynamic)?> = StaticProp(element.ondrag)
    open var onDragEnd: Prop<((DragEvent) -> dynamic)?> = StaticProp(element.ondragend)
    open var onDragEnter: Prop<((DragEvent) -> dynamic)?> = StaticProp(element.ondragenter)
    open var onDragExit: Prop<((DragEvent) -> dynamic)?> = StaticProp(element.ondragexit)
    open var onDragLeave: Prop<((DragEvent) -> dynamic)?> = StaticProp(element.ondragleave)
    open var onDragOver: Prop<((DragEvent) -> dynamic)?> = StaticProp(element.ondragover)
    open var onDragStart: Prop<((DragEvent) -> dynamic)?> = StaticProp(element.ondragstart)
    open var onDrop: Prop<((DragEvent) -> dynamic)?> = StaticProp(element.ondrop)
    open var onDurationChange: Prop<((Event) -> dynamic)?> = StaticProp(element.ondurationchange)
    open var onEmptied: Prop<((Event) -> dynamic)?> = StaticProp(element.onemptied)
    open var onEnded: Prop<((Event) -> dynamic)?> = StaticProp(element.onended)
    open var onError: Prop<((dynamic, String, Int, Int, Any?) -> dynamic)?> = StaticProp(element.onerror)
    open var onFocus: Prop<((FocusEvent) -> dynamic)?> = StaticProp(element.onfocus)
    open var onInput: Prop<((InputEvent) -> dynamic)?> = StaticProp(element.oninput)
    open var onInvalid: Prop<((Event) -> dynamic)?> = StaticProp(element.oninvalid)
    open var onKeydown: Prop<((KeyboardEvent) -> dynamic)?> = StaticProp(element.onkeydown)
    open var onKeypress: Prop<((KeyboardEvent) -> dynamic)?> = StaticProp(element.onkeypress)
    open var onKeyup: Prop<((KeyboardEvent) -> dynamic)?> = StaticProp(element.onkeyup)
    open var onLoad: Prop<((Event) -> dynamic)?> = StaticProp(element.onload)
    open var onLoadedData: Prop<((Event) -> dynamic)?> = StaticProp(element.onloadeddata)
    open var onLoadedMetadata: Prop<((Event) -> dynamic)?> = StaticProp(element.onloadedmetadata)
    open var onLoadEnd: Prop<((Event) -> dynamic)?> = StaticProp(element.onloadend)
    open var onLoadStart: Prop<((ProgressEvent) -> dynamic)?> = StaticProp(element.onloadstart)
    open var onMouseDown: Prop<((MouseEvent) -> dynamic)?> = StaticProp(element.onmousedown)
    open var onMouseEnter: Prop<((MouseEvent) -> dynamic)?> = StaticProp(element.onmouseenter)
    open var onMouseLeave: Prop<((MouseEvent) -> dynamic)?> = StaticProp(element.onmouseleave)
    open var onMouseMove: Prop<((MouseEvent) -> dynamic)?> = StaticProp(element.onmousemove)
    open var onMouseOut: Prop<((MouseEvent) -> dynamic)?> = StaticProp(element.onmouseout)
    open var onMouseOver: Prop<((MouseEvent) -> dynamic)?> = StaticProp(element.onmouseover)
    open var onMouseUp: Prop<((MouseEvent) -> dynamic)?> = StaticProp(element.onmouseup)
    open var onWheel: Prop<((WheelEvent) -> dynamic)?> = StaticProp(element.onwheel)
    open var onPause: Prop<((Event) -> dynamic)?> = StaticProp(element.onpause)
    open var onPlay: Prop<((Event) -> dynamic)?> = StaticProp(element.onplay)
    open var onPlaying: Prop<((Event) -> dynamic)?> = StaticProp(element.onplaying)
    open var onProgress: Prop<((ProgressEvent) -> dynamic)?> = StaticProp(element.onprogress)
    open var onRateChange: Prop<((Event) -> dynamic)?> = StaticProp(element.onratechange)
    open var onReset: Prop<((Event) -> dynamic)?> = StaticProp(element.onreset)
    open var onResize: Prop<((Event) -> dynamic)?> = StaticProp(element.onresize)
    open var onScroll: Prop<((Event) -> dynamic)?> = StaticProp(element.onscroll)
    open var onSeeked: Prop<((Event) -> dynamic)?> = StaticProp(element.onseeked)
    open var onSeeking: Prop<((Event) -> dynamic)?> = StaticProp(element.onseeking)
    open var onSelect: Prop<((Event) -> dynamic)?> = StaticProp(element.onselect)
    open var onShow: Prop<((Event) -> dynamic)?> = StaticProp(element.onshow)
    open var onStalled: Prop<((Event) -> dynamic)?> = StaticProp(element.onstalled)
    open var onSubmit: Prop<((Event) -> dynamic)?> = StaticProp(element.onsubmit)
    open var onSuspend: Prop<((Event) -> dynamic)?> = StaticProp(element.onsuspend)
    open var onTimeUpdate: Prop<((Event) -> dynamic)?> = StaticProp(element.ontimeupdate)
    open var onToggle: Prop<((Event) -> dynamic)?> = StaticProp(element.ontoggle)
    open var onVolumeChange: Prop<((Event) -> dynamic)?> = StaticProp(element.onvolumechange)
    open var onWaiting: Prop<((Event) -> dynamic)?> = StaticProp(element.onwaiting)
    open var onGotPointerCapture: Prop<((PointerEvent) -> dynamic)?> = StaticProp(element.ongotpointercapture)
    open var onLostPointerCapture: Prop<((PointerEvent) -> dynamic)?> = StaticProp(element.onlostpointercapture)
    open var onPointerDown: Prop<((PointerEvent) -> dynamic)?> = StaticProp(element.onpointerdown)
    open var onPointerMove: Prop<((PointerEvent) -> dynamic)?> = StaticProp(element.onpointermove)
    open var onPointerUp: Prop<((PointerEvent) -> dynamic)?> = StaticProp(element.onpointerup)
    open var onPointerCancel: Prop<((PointerEvent) -> dynamic)?> = StaticProp(element.onpointercancel)
    open var onPointerOver: Prop<((PointerEvent) -> dynamic)?> = StaticProp(element.onpointerover)
    open var onPointerOut: Prop<((PointerEvent) -> dynamic)?> = StaticProp(element.onpointerout)
    open var onPointerEnter: Prop<((PointerEvent) -> dynamic)?> = StaticProp(element.onpointerenter)
    open var onPointerLeave: Prop<((PointerEvent) -> dynamic)?> = StaticProp(element.onpointerleave)
    open var onCopy: Prop<((ClipboardEvent) -> dynamic)?> = StaticProp(element.oncopy)
    open var onCut: Prop<((ClipboardEvent) -> dynamic)?> = StaticProp(element.oncut)
    open var onPaste: Prop<((ClipboardEvent) -> dynamic)?> = StaticProp(element.onpaste)
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

open class DefaultWindowEventHandlerProps<E : WindowEventHandlers>(
    protected val windowElement: E
) : DefaultHTMLElementProps<HTMLElement>(windowElement as HTMLElement) {
    open var onAfterPrint: Prop<((Event) -> dynamic)?> = StaticProp(windowElement.onafterprint)
    open var onBeforePrint: Prop<((Event) -> dynamic)?> = StaticProp(windowElement.onbeforeprint)
    open var onBeforeUnload: Prop<((BeforeUnloadEvent) -> String?)?> = StaticProp(windowElement.onbeforeunload)
    open var onHashChange: Prop<((HashChangeEvent) -> dynamic)?> = StaticProp(windowElement.onhashchange)
    open var onLanguageChange: Prop<((Event) -> dynamic)?> = StaticProp(windowElement.onlanguagechange)
    open var onMessage: Prop<((MessageEvent) -> dynamic)?> = StaticProp(windowElement.onmessage)
    open var onOffline: Prop<((Event) -> dynamic)?> = StaticProp(windowElement.onoffline)
    open var onOnline: Prop<((Event) -> dynamic)?> = StaticProp(windowElement.ononline)
    open var onPageHide: Prop<((PageTransitionEvent) -> dynamic)?> = StaticProp(windowElement.onpagehide)
    open var onPageShow: Prop<((PageTransitionEvent) -> dynamic)?> = StaticProp(windowElement.onpageshow)
    open var onPopState: Prop<((PopStateEvent) -> dynamic)?> = StaticProp(windowElement.onpopstate)
    open var onRejectionHandled: Prop<((Event) -> dynamic)?> = StaticProp(windowElement.onrejectionhandled)
    open var onStorage: Prop<((StorageEvent) -> dynamic)?> = StaticProp(windowElement.onstorage)
    open var onUnhandledRejection: Prop<((PromiseRejectionEvent) -> dynamic)?> =
        StaticProp(windowElement.onunhandledrejection)
    open var onUnload: Prop<((Event) -> dynamic)?> = StaticProp(windowElement.onunload)

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
package elementix.dom.view

import elementix.reactivity.Context

fun interface IntoView {
    fun intoView(): View
}
sealed interface View: Mountable, IntoView {
    override fun intoView(): View = this


}
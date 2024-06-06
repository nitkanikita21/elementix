package elementix.trpc.route

import elementix.trpc.utils.toKebabCase

open class Route(name: String? = null) {
    val name: String = name ?: this::class.simpleName!!.toKebabCase()

    val procedures: MutableMap<String, Procedure<in Any, out Any>> = mutableMapOf()
}

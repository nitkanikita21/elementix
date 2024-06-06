package elementix.trpc.builder

import kotlin.properties.Delegates
import kotlin.reflect.KProperty

fun interface GetDelegate<V> {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): V
}

package elementix.utils

import kotlin.reflect.KProperty

fun interface GetDelegate<T : Any?, V> {
    operator fun getValue(thisRef: T, property: KProperty<*>): V
}

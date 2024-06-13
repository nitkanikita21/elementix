package elementix.utils

import kotlin.reflect.KProperty

fun interface SetDelegate<T : Any?, V> {
    operator fun setValue(thisRef: T, property: KProperty<*>, value: V)
}

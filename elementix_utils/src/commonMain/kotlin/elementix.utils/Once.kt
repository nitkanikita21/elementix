package elementix.utils

class Once<T> {
    private var value: Any = UNINITIALIZED_VALUE

    fun get(): T? =
        if (value == UNINITIALIZED_VALUE) null else value as T
    fun getOrDefault(default: T): T =
        if (value == UNINITIALIZED_VALUE) default.also { value = it as Any } else value as T
    fun getOrInit(init: () -> T): T =
        if (value == UNINITIALIZED_VALUE) init().also { value = it as Any } else value as T

    @Suppress("ClassName")
    private object UNINITIALIZED_VALUE
}

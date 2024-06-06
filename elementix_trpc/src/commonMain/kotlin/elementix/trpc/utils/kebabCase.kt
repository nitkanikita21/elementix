package elementix.trpc.utils

fun String.toKebabCase() = this.split("(?=[A-Z])".toRegex()).joinToString("-") { it.lowercase() }
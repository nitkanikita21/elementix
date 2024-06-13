package elementix.utils

interface GetSetDelegate<C: Any?, T>: GetDelegate<C, T>,  SetDelegate<C, T> {
}
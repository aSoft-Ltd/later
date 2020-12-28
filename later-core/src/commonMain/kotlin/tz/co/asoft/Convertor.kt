package tz.co.asoft

fun interface Convertor<F, T> {
    fun convert(obj: F): T
}
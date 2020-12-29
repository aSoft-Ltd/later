package tz.co.asoft

fun interface Callback<F, T> {
    fun invoke(obj: F): T
}
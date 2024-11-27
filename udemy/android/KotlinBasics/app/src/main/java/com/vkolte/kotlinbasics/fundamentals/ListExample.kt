package com.vkolte.kotlinbasics.fundamentals

fun main() {
    val month = listOf("January", "February", "March")
    val anyType = listOf(1, 2, true, "Vaibhav", month)

    println(month)
    println(anyType)

    for (element in anyType) {
        println("Is $element type of list ${element is List<*>}")
    }

    val additionalMonth = month.toMutableList()
    println(additionalMonth)
}
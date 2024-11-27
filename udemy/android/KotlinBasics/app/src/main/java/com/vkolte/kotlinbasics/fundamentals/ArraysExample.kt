package com.vkolte.kotlinbasics.fundamentals

fun main() {
    val numbers = intArrayOf(1, 2, 3, 4, 5, 6)
    println(numbers)
    println(numbers.contentToString())


    val array = arrayOf(1, 2, "Vaibhav", false, 'V')
    println(array.contentToString())
}
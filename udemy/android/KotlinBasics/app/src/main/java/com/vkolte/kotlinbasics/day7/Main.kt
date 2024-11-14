package com.vkolte.kotlinbasics.day7

fun main() {
    val numbers = listOf(1, 2, 3)
    println(numbers)
    val doubles = numbers.map { it * 2 }
    println(doubles)
}
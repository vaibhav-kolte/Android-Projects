package com.vkolte.kotlinbasics.day4

fun main() {
    val fruitsName = mutableListOf("Apple", "Guava", "Mango", "Orange", "Banana")
    println(fruitsName)
    fruitsName.add("Strawberry")
    println(fruitsName)
    fruitsName.removeAt(2)
    println(fruitsName)

    if (fruitsName.contains("Orange")) {
        println("Orange is contains in this fruits list.")
    } else {
        println("Orange is not contains in this fruits list.")
    }
}
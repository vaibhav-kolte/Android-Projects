package com.vkolte.kotlinbasics.fundamentals

fun main() {
    val number = arrayListOf(1.0, 2.0, 3.0, 4.0)

//    var sumNumber = 0.0
//    number.forEach {
//        sumNumber += it
//    }
//
//    val average = sumNumber / number.size
//    println("Average of give array list is $average")

    val sum = number.sum()
    println(sum / number.size)
}
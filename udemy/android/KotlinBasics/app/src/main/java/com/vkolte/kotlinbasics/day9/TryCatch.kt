package com.vkolte.kotlinbasics.day9

fun main() {
    try {
        print("Enter number: ")
        val number = readln().toInt()
        println("User entered number $number")
    } catch (e: NumberFormatException) {
        println("NumberFormatException: ${e.message}")
    }finally {
        println("This will be executed regardless. Error or No error")
    }
}
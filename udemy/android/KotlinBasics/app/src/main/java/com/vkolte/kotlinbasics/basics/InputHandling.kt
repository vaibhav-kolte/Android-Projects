package com.vkolte.kotlinbasics.basics

fun main() {
    println("Please enter a number: ")
    val inputString = readln()
    val inputNumber = inputString.toInt()
    val multiplier = 5
    println("Multiplication ${inputNumber * multiplier}")
}
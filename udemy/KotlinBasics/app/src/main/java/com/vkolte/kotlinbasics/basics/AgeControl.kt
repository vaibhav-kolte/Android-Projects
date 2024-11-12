package com.vkolte.kotlinbasics.basics

fun main() {

    print("Enter your age as a whole number: ")

    val age = readln().toInt()

    if (age in 18..39) {
        println("You can enter the club.")
    } else if (age > 40) {
        println("You cannot go into the club, please go home.")
    } else {
        println("Age not verified. Please contact support.")
    }
}
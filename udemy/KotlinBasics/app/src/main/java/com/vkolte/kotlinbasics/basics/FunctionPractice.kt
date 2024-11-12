package com.vkolte.kotlinbasics.basics

data class CoffeeDetails(
    val sugarCount: Int,
    val name: String,
    val size: String,
    val creamAmount: Int
)

fun main() {

    print("Enter num1: ")
    val num1 = readln().toInt()
    print("Enter num3: ")
    val num2 = readln().toInt()

    val result = add(num1, num2)
    print("Sum of $num1 + $num2 is $result")

}

fun add(num1: Int, num2: Int): Int {
    val result = num1 + num2
    return result
}

fun askCoffeeDetails() {
    print("Who is this coffee for? ")
    val name = readln()
    print("How many pieces of sugar do you want? ")
    val sugarCount = readln().toInt()

    // Call Function
    makeCoffee(name, sugarCount)

//    makeCoffee("Sagar", 2)
//
//    makeCoffee("Yatish", 0)
//
//    makeCoffee("Satish", 30)

    val coffeeForVaibhav = CoffeeDetails(0, "Vaibhav", "XXL", 2)
}

// Define Function
fun makeCoffee(name: String, sugarCount: Int) {
    if (sugarCount == 0) {
        println("Coffee with no sugar for $name")
    } else
        println("Coffee with $sugarCount ${if (sugarCount == 1) "spoon" else "spoons"} of sugar for $name")
}
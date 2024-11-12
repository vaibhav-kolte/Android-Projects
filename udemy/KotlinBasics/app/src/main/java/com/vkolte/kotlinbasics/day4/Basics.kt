package com.vkolte.kotlinbasics.day4

fun main() {
    // Immutable list
    val immutableShoppingCard = listOf("Processor", "RAM", "Graphics Card", "SSD")
//    immutableShoppingCard.add("Keyboard")


    // Mutable list
    val mutableShoppingList = mutableListOf("Processor", "RAM", "Graphics Card", "SSD")
    mutableShoppingList.add("Keyboard")
//    println(mutableShoppingList)

    mutableShoppingList.set(3, "Water cooling")
//    println(mutableShoppingList)
    if (mutableShoppingList.contains("RAM")) {
//        println("Has RAM in the list")
    } else {
//        println("RAM is not in the list")
    }

    for (item in mutableShoppingList) {
        println(item)
        if (item == "RAM") break
    }

}
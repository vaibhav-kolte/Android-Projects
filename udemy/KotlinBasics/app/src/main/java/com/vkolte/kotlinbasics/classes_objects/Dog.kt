package com.vkolte.kotlinbasics.classes_objects

class Dog(val name: String) {
    init {
        bark(name)
    }

    private fun bark(name: String) {
        println("$name says woof woof")
    }
}
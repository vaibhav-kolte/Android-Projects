package com.vkolte.kotlinbasics.day7

fun main() {
    // nullable String
    val name: String? = "Vaibhav"
    name?.let {
        println(it.uppercase())
    }
}
package com.vkolte.kotlinbasics.classes_objects

fun main() {
    val book = Book()
    println(book.title)
    println(book.author)
    println(book.yearPublish)

    val customBook = Book("Good Vibes Good Life", "Vex King", 2018)
    println(customBook.title)
    println(customBook.author)
    println(customBook.yearPublish)
}
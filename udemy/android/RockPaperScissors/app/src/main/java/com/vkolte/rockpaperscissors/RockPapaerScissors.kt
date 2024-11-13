package com.vkolte.rockpaperscissors

fun main() {
    var computerChoice = ""
    var playerChoice: String
    println("Rock, Paper or Scissors? Enter your choice!")
    while (true) {
        playerChoice = readlnOrNull().toString()
        if (playerChoice == "Rock" || playerChoice == "Paper" || playerChoice == "Scissors")
            break
        else
            println("Please enter valid choice: ")
    }
    val randomNumber = (1..3).random()

    when (randomNumber) {
        1 -> {
            computerChoice = "Rock"
        }

        2 -> {
            computerChoice = "Paper"
        }

        3 -> {
            computerChoice = "Scissors"
        }
    }
    println(computerChoice)

    val winner = when {
        playerChoice == computerChoice -> "Tie"
        playerChoice == "Rock" && computerChoice == "Scissors" -> "Player"
        playerChoice == "Paper" && computerChoice == "Rock" -> "Player"
        playerChoice == "Scissors" && computerChoice == "Paper" -> "Player"
        else -> "Computer"
    }

    if (winner == "Tie") {
        println("It's a tie")
    } else {
        println("$winner won!")
    }
}
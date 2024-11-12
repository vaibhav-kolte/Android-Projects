package com.vkolte.bankaccountprogram

class BankAccount(
    private val accountHolder: String,
    private var balance: Double
) {

    private var transactionHistory = mutableListOf<String>()

    fun deposit(amount: Double) {
        balance += amount
        transactionHistory.add("$accountHolder deposited $$amount")
    }

    fun withdraw(amount: Double) {
        if (amount <= balance) {
            // we can withdraw
            balance -= amount
            transactionHistory.add("$accountHolder withdraw amount $$amount")
        } else {
            // we can not withdraw
            println("You don't have the funds to withdraw the amount $$amount")
        }
    }

    fun displayTransactionHistory() {
        println("Transaction history for $accountHolder")
        for (transaction in transactionHistory) {
            println(transaction)
        }
    }

    fun acctBalance(){
        println("\n${accountHolder}'s available balance is ${balance}")
    }
}
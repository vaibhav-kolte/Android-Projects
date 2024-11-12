package com.vkolte.bankaccountprogram

fun main() {

    val vaibhavsBankAccount = BankAccount("Vaibhav", 1234.25)
    vaibhavsBankAccount.withdraw(2000.0)
    vaibhavsBankAccount.deposit(200.0)
    vaibhavsBankAccount.withdraw(500.0)
    vaibhavsBankAccount.withdraw(50.15)
    vaibhavsBankAccount.deposit(1000.0)
    vaibhavsBankAccount.displayTransactionHistory()

    vaibhavsBankAccount.acctBalance()
    println("-------------------------------------------------")
    val sagarBankAccount = BankAccount("Sager", 100.0)

    sagarBankAccount.withdraw(100.0)
    sagarBankAccount.deposit(800.0)
    sagarBankAccount.withdraw(1500.0)
    sagarBankAccount.withdraw(300.0)
    sagarBankAccount.displayTransactionHistory()

    sagarBankAccount.acctBalance()
}
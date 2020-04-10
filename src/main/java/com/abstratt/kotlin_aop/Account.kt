package com.abstratt.kotlin_aop

data class Account(var balance : Int = 1000) {
    @Debuggable
    fun withdraw(amount: Int): Boolean {
        if (balance < amount) {
            return false
        }
        balance = balance - amount
        return true
    }
}
package com.vkolte.kotlinbasics.day8

class SecondaryClass : BaseClass() {

    override fun role() {
        super.role()
        println("Member of the house in Secondary class")
    }
}
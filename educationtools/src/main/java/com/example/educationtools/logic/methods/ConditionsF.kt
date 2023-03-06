package com.example.educationtools.logic.methods

class ConditionsF {
    companion object {
        //Less
        fun lessII(x: Int, y: Int): Boolean {
            return x < y
        }
        fun lessFI(x: Float, y: Int): Boolean {
            return x < y
        }
        fun lessIF(x: Int, y: Float): Boolean {
            return x < y
        }
        fun lessFF(x: Float, y: Float): Boolean {
            return x < y
        }
        //More
        fun moreII(x: Int, y: Int): Boolean {
            return x > y
        }
        fun moreFI(x: Float, y: Int): Boolean {
            return x > y
        }
        fun moreIF(x: Int, y: Float): Boolean {
            return x > y
        }
        fun moreFF(x: Float, y: Float): Boolean {
            return x > y
        }
    }
}
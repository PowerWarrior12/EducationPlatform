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

        //MoreOrEqual
        fun moreOrEqualII(x: Int, y: Int): Boolean {
            return x >= y
        }
        fun moreOrEqualFI(x: Float, y: Int): Boolean {
            return x >= y
        }
        fun moreOrEqualIF(x: Int, y: Float): Boolean {
            return x >= y
        }
        fun moreOrEqualFF(x: Float, y: Float): Boolean {
            return x >= y
        }

        //LessOrEqual
        fun lessOrEqualII(x: Int, y: Int): Boolean {
            return x <= y
        }
        fun lessOrEqualFI(x: Float, y: Int): Boolean {
            return x <= y
        }
        fun lessOrEqualIF(x: Int, y: Float): Boolean {
            return x <= y
        }
        fun lessOrEqualFF(x: Float, y: Float): Boolean {
            return x <= y
        }

        //Equal
        fun equalII(x: Int, y: Int): Boolean {
            return x == y
        }
        fun equalFI(x: Float, y: Int): Boolean {
            return x == y.toFloat()
        }
        fun equalIF(x: Int, y: Float): Boolean {
            return x.toFloat() == y
        }
        fun equalFF(x: Float, y: Float): Boolean {
            return x == y
        }

        //Not Equal
        fun notEqualII(x: Int, y: Int): Boolean {
            return x != y
        }
        fun notEqualFI(x: Float, y: Int): Boolean {
            return x != y.toFloat()
        }
        fun notEqualIF(x: Int, y: Float): Boolean {
            return x.toFloat() != y
        }
        fun notEqualFF(x: Float, y: Float): Boolean {
            return x != y
        }
        //And
        fun andF(x: Boolean, y: Boolean): Boolean {
            return x && y
        }
        //Or
        fun orF(x: Boolean, y: Boolean): Boolean {
            return x || y
        }
    }
}
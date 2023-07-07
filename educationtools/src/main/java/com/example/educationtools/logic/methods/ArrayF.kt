package com.example.educationtools.logic.methods

class ArrayF {
    companion object {
        fun getI(array: IntArray, i: Int): Int {
            return array[i]
        }

        fun getF(array: FloatArray, i: Int): Float {
            return array[i]
        }

        fun setI(array: IntArray, i: Int, value: Int) {
            array[i] = value
        }

        fun setF(array: FloatArray, i: Int, value: Float) {
            array[i] = value
        }
    }
}
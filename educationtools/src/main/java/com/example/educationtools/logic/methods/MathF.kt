package com.example.educationtools.logic.methods

class MathF {
    companion object {
        //Sum
        fun sumII(x: Int, y: Int): Int {
            return x + y
        }

        fun sumIF(x: Int, y: Float): Float {
            return x + y
        }

        fun sumFF(x: Float, y: Float): Float {
            return x + y
        }

        fun sumFI(x: Float, y: Int): Float {
            return x + y
        }

        //Minus
        fun minusII(x: Int, y: Int): Int {
            return sumII(x, -y)
        }
        fun minusIF(x: Int, y: Float): Float {
            return sumIF(x, -y)
        }
        fun minusFF(x: Float, y: Float): Float {
            return sumFF(x, -y)
        }
        fun minusFI(x: Float, y: Int): Float {
            return sumFI(x, -y)
        }

        //Multiply

        fun timeII(x: Int, y: Int): Int {
            return x * y
        }

        fun timeIF(x: Int, y: Float): Float {
            return x * y
        }
        fun timeFF(x: Float, y: Float): Float {
            return x * y
        }
        fun timeFI(x: Float, y: Int): Float {
            return x * y
        }

        //Division

        fun divII(x: Int, y: Int): Int {
            return x / y
        }
        fun divIF(x: Int, y: Float): Float {
            return x / y
        }
        fun divFF(x: Float, y: Float): Float {
            return x / y
        }
        fun divFI(x: Float, y: Int): Float {
            return x / y
        }

        //Remains

        fun modII(x: Int, y: Int) : Int {
            return x.mod(y)
        }
    }
}
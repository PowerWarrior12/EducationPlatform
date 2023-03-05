package com.example.educationtools.examples

import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.educationtools.R
import com.example.educationtools.base.EditorViewBase
import com.example.educationtools.blocks.NotifyBlock
import com.example.educationtools.logic.*
import java.lang.Double.sum
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.jvm.ExperimentalReflectionOnLambdas
import kotlin.reflect.jvm.reflect

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val textOne = findViewById<EditText>(R.id.textView)
        val textTwo = findViewById<EditText>(R.id.textView2)

        val editor = findViewById<EditorViewBase>(R.id.editor)

        val list = listOf<Int>(1, 2, 3, 4, 5, 6, 8)
        Log.d("Block-Shames", list.takeWhile { it > 4 }.toString())

        val memoryModel = MemoryModel2()
        memoryModel.declareConditionBlock("1")
        memoryModel.declareWhileDoBlock("2")
        memoryModel.declareVarBlock("3")
        memoryModel.declareVarBlock("4")
        memoryModel.declareDoWhileBlock("5")
        memoryModel.declareConditionBlock("6")
        memoryModel.declareVarBlock("7")
        memoryModel.declareVarBlock("8")
        memoryModel.declareVarBlock("9")
        memoryModel.declareVarBlock("10")
        memoryModel.declareVarBlock("11")
        memoryModel.declareVarBlock("12")
        memoryModel.declareWhileDoBlock("13")
        memoryModel.declareVarBlock("14")
        memoryModel.declareDoWhileBlock("15")
        memoryModel.declareConditionBlock("16")
        memoryModel.declareVarBlock("17")
        memoryModel.declareVarBlock("18")
        memoryModel.declareVarBlock("19")
        memoryModel.declareVarBlock("20")
        memoryModel.declareVarBlock("21")
        memoryModel.declareWhileDoBlock("22")
        memoryModel.declareVarBlock("23")
        memoryModel.declareVarBlock("24")
        memoryModel.declareDoWhileBlock("25")

        memoryModel.bindBlocksOrThrow("1", "2", false)
        memoryModel.bindBlocksOrThrow("2", "3", true)
        memoryModel.bindBlocksOrThrow("4", "5", null)
        memoryModel.bindBlocksOrThrow("5", "4", true)
        memoryModel.bindBlocksOrThrow("3", "4", null)
        memoryModel.bindBlocksOrThrow("5", "2", false)
        memoryModel.bindBlocksOrThrow("1", "11", true)
        memoryModel.bindBlocksOrThrow("11", "12", null)
        memoryModel.bindBlocksOrThrow("12", "13", null)
        memoryModel.bindBlocksOrThrow("13", "14", true)
        memoryModel.bindBlocksOrThrow("14", "13", null)
        memoryModel.bindBlocksOrThrow("13", "15", false)
        memoryModel.bindBlocksOrThrow("15", "16", false)
        memoryModel.bindBlocksOrThrow("16", "17", false)
        memoryModel.bindBlocksOrThrow("15", "12", true)
        memoryModel.bindBlocksOrThrow("17", "18", null)
        memoryModel.bindBlocksOrThrow("18", "19", null)
        memoryModel.bindBlocksOrThrow("16", "20", true)
        memoryModel.bindBlocksOrThrow("21", "22", null)
        memoryModel.bindBlocksOrThrow("22", "23", true)
        memoryModel.bindBlocksOrThrow("23", "22", null)
        memoryModel.bindBlocksOrThrow("22", "24", false)
        memoryModel.bindBlocksOrThrow("24", "25", null)
        memoryModel.bindBlocksOrThrow("25", "21", true)
        memoryModel.bindBlocksOrThrow("20", "21", null)
        memoryModel.bindBlocksOrThrow("6", "7", false)
        memoryModel.bindBlocksOrThrow("7", "8", null)
        memoryModel.bindBlocksOrThrow("6", "9", true)
        memoryModel.bindBlocksOrThrow("9", "10", null)
        memoryModel.bindBlocksOrThrow("2", "6", false)

        memoryModel.declareDoWhileBlock("26")
        memoryModel.bindBlocksOrThrow("25", "2", false)
        memoryModel.bindBlocksOrThrow("8", "26", null)

    }
}
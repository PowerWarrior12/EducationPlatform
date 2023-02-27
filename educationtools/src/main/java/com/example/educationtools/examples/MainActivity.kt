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
        val block = NotifyBlock()
        editor.addChild(block)
        block.apply {
            updatePosition(PointF(500f, 500f))
            updateSize(500f, 200f)
        }

        val x = 4
        val y = 5
        val z = x + y

        val block2 = NotifyBlock()
        editor.addChild(block2)
        block2.apply {
            updatePosition(PointF(500f, 800f))
            updateSize(500f, 200f)
        }
    }
}
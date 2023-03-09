package com.example.educationtools.examples

import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.educationtools.R
import com.example.educationtools.base.EditorViewBase
import com.example.educationtools.blocks.*
import com.example.educationtools.logic.*
import com.example.educationtools.logic.functions.ConditionFunction
import com.example.educationtools.logic.functions.ReflectFunction
import com.example.educationtools.logic.functions.TypeFunction
import com.example.educationtools.logic.functions.VariableFunction
import com.example.educationtools.logic.methods.ArrayF
import com.example.educationtools.logic.methods.ConditionsF
import com.example.educationtools.logic.methods.MathF
import com.example.educationtools.logic.parsers.StartBlockParser

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val textOne = findViewById<EditText>(R.id.textView)
        val textTwo = findViewById<EditText>(R.id.textView2)

        val editor = findViewById<EditorViewBase>(R.id.editor)

        editor.addChild(CalculationBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(300f, 700f))
            updateSize(500f, 300f)
        })

        editor.addChild(ConditionBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(500f, 900f))
            updateSize(500f, 300f)
        })

        editor.addChild(ConditionBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(500f, 900f))
            updateSize(500f, 300f)
        })
        val startBlockParser = StartBlockParser()
        val text1 = "int   x  ,  float   y, int Jopa"
        val text2 = ","
        val text3 = "int roma, "
        val text4 = "ivan vanya, int x"
        val text5 = "    int     4sd"
        val text6 = "      float    FSF_323  "
        val text7 = "int fdg asdf, int x, int y"
        try {
            Log.d("MyTag", startBlockParser.parseOrThrow(text1).toString())
        } catch (e: java.lang.Exception) {}
        try {
            Log.d("MyTag", startBlockParser.parseOrThrow(text2).toString())
        } catch (e: java.lang.Exception) {}
        try {
            Log.d("MyTag", startBlockParser.parseOrThrow(text3).toString())
        } catch (e: java.lang.Exception) {}
        try {
            Log.d("MyTag", startBlockParser.parseOrThrow(text4).toString())
        } catch (e: java.lang.Exception) {}
        try {
            Log.d("MyTag", startBlockParser.parseOrThrow(text5).toString())
        } catch (e: java.lang.Exception) {}
        try {
            Log.d("MyTag", startBlockParser.parseOrThrow(text6).toString())
        } catch (e: java.lang.Exception) {}
        try {
            Log.d("MyTag", startBlockParser.parseOrThrow(text7).toString())
        } catch (e: java.lang.Exception) {}

    }
}
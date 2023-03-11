package com.example.educationtools.examples

import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.example.educationtools.logic.parsers.CalculationBlockParser
import com.example.educationtools.logic.parsers.PostfixExpressionParser
import com.example.educationtools.logic.parsers.StartBlockParser
import com.example.educationtools.utils.extensions.split

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        button.visibility = View.GONE
        val textOne = findViewById<EditText>(R.id.textView)
        textOne.visibility = View.GONE
        val textTwo = findViewById<EditText>(R.id.textView2)
        textTwo.visibility = View.GONE

        val editor = findViewById<EditorViewBase>(R.id.editor)

        editor.addChild(CalculationBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(0f, 0f))
            updateSize(400f, 250f)
        })

        editor.addChild(CalculationBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(0f, 300f))
            updateSize(400f, 250f)
        })

        editor.addChild(ConditionBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(0f, 600f))
            updateSize(400f, 250f)
        })

        editor.addChild(ConditionBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(0f, 900f))
            updateSize(400f, 250f)
        })

        editor.addChild(WhileDoBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(0f, 1200f))
            updateSize(400f, 250f)
        })

        editor.addChild(StartBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(500f, 0f))
            updateSize(400f, 250f)
        })

        editor.addChild(CalculationBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(500f, 300f))
            updateSize(400f, 250f)
        })

        editor.addChild(CalculationBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(500f, 600f))
            updateSize(400f, 250f)
        })

        editor.addChild(CalculationBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(500f, 900f))
            updateSize(400f, 250f)
        })

        editor.addChild(EndBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(500f, 1200f))
            updateSize(400f, 250f)
        })

        editor.addChild(CalculationBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(1000f, 0f))
            updateSize(400f, 250f)
        })

        editor.addChild(CalculationBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(1000f, 300f))
            updateSize(400f, 250f)
        })

        editor.addChild(ConditionBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(1000f, 600f))
            updateSize(400f, 250f)
        })

        editor.addChild(WhileDoBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(1000f, 900f))
            updateSize(400f, 250f)
        })

        editor.addChild(CalculationBlockView().apply {
            setEditorParent(editor)
            updatePosition(PointF(1000f, 1200f))
            updateSize(400f, 250f)
        })

    }
}
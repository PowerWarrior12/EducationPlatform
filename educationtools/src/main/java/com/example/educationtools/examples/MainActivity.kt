package com.example.educationtools.examples

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.educationtools.R
import com.example.educationtools.base.EditorViewBase
import com.example.educationtools.blocks.NotifyBlock
import com.example.educationtools.logic.*
import com.example.educationtools.logic.functions.ConditionFunction
import com.example.educationtools.logic.functions.ReflectFunction
import com.example.educationtools.logic.functions.TypeFunction
import com.example.educationtools.logic.functions.VariableFunction
import com.example.educationtools.logic.methods.ArrayF
import com.example.educationtools.logic.methods.ConditionsF
import com.example.educationtools.logic.methods.MathF

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val textOne = findViewById<EditText>(R.id.textView)
        val textTwo = findViewById<EditText>(R.id.textView2)

        val editor = findViewById<EditorViewBase>(R.id.editor)

        editor.addChild(NotifyBlock().apply {
            setEditorParent(editor)
            updateSize(500f, 300f)
        })
    }
}
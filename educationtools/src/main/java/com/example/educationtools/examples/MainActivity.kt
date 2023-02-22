package com.example.educationtools.examples

import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.educationtools.R
import com.example.educationtools.base.EditableBlockBase
import com.example.educationtools.base.EditorViewBase
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val textOne = findViewById<EditText>(R.id.textView)
        val textTwo = findViewById<EditText>(R.id.textView2)
    }
}
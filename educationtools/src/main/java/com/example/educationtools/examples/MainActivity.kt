package com.example.educationtools.examples

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationtools.R
import com.example.educationtools.base.EditableBlockBase
import com.example.educationtools.base.EditorViewBase
import com.example.educationtools.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()
    private var text = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editor = findViewById<EditorViewBase>(R.id.editor)

        binding.button2.setOnClickListener {
            editor.start(text)
        }
        editor.addOnSuccessListener {
            Toast.makeText(this, it.fold("") { result_text, s ->
                "$result_text ${s.name} = ${s.value}"
            }, Toast.LENGTH_LONG).show()
        }

        editor.addOnErrorListener {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        binding.editText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.recycler.apply {
            adapter = editor.generateMenuAdapter()
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.HORIZONTAL))
        }

        editor.addOnBlockDoubleTouchListener {
            showDialog(it)
        }
    }

    private fun showDialog(block: EditableBlockBase) {
        val newFragment: TextDialogFragment = TextDialogFragment.newInstance(block)
        newFragment.show(supportFragmentManager, "dialog")
    }

}
package com.example.educationtools.examples

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationtools.R
import com.example.educationtools.base.EditableBlockBase
import com.example.educationtools.base.EditorViewBase
import com.example.educationtools.blocks.*
import com.example.educationtools.databinding.ActivityMainBinding
import com.example.educationtools.logic.*

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()
    private var text = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editor = findViewById<EditorViewBase>(R.id.editor)
        val saveData = "{\"blocks\":[{\"type\":\"StartType\",\"center_x\":717.0,\"center_y\":255.0,\"width\":400.0,\"height\":250.0,\"text\":\"int x\",\"output_knot\":{\"id\":\"8097b764-b6aa-408d-9584-46791dadeec9\"},\"id\":\"70ac1f72-2f03-47e8-9c00-eed33e5724e8\"},{\"type\":\"CalculationType\",\"center_x\":717.38336,\"center_y\":788.6167,\"width\":400.0,\"height\":250.0,\"text\":\"y = sum(x, 5)\",\"left_knot\":{\"id\":\"d7258234-20ba-4427-9ba9-8baf01712d21\"},\"right_knot\":{\"id\":\"9c03e451-ec98-4bf1-ac0a-c461060c0c04\"},\"top_knot\":{\"id\":\"ad86ba45-651f-42de-beaf-f2680ed009a1\"},\"bottom_knot\":{\"id\":\"251817f0-2964-4a18-a979-beb4a9e3614b\"},\"id\":\"75975c52-040d-4d84-b461-255978ea2c12\"},{\"type\":\"EndType\",\"center_x\":752.0,\"center_y\":1334.0,\"width\":400.0,\"height\":250.0,\"text\":\"y\",\"input_knot\":{\"id\":\"31dc4ab5-05cb-4762-85bf-ac6cce65148b\"},\"id\":\"9f1aa71e-8c2a-42f0-8f35-773e6dfd6f6e\"}],\"connections\":[{\"start_knot_id\":\"8097b764-b6aa-408d-9584-46791dadeec9\",\"start_block_id\":\"70ac1f72-2f03-47e8-9c00-eed33e5724e8\",\"end_knot_id\":\"ad86ba45-651f-42de-beaf-f2680ed009a1\",\"end_block_id\":\"75975c52-040d-4d84-b461-255978ea2c12\"},{\"start_knot_id\":\"251817f0-2964-4a18-a979-beb4a9e3614b\",\"start_block_id\":\"75975c52-040d-4d84-b461-255978ea2c12\",\"end_knot_id\":\"31dc4ab5-05cb-4762-85bf-ac6cce65148b\",\"end_block_id\":\"9f1aa71e-8c2a-42f0-8f35-773e6dfd6f6e\"}]}"
        //editor.loadConfigurations(saveData)
        binding.button2.setOnClickListener {
            editor.start(text)
            Log.d("SAVEEEEE", editor.saveConfigurations())
        }
        editor.addOnSuccessListener {
            Toast.makeText(this, it.fold("") { acc, s ->
                "$acc, ${s.name} = ${s.value}"
            }, Toast.LENGTH_LONG).show()
        }

        editor.addOnErrorListener {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        binding.editText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
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
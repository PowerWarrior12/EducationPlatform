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
import com.example.educationtools.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()
    private var text = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editor = findViewById<EditorViewBase>(R.id.editor)
        val saveData = "{\"blocks\":[{\"type\":\"StartType\",\"center_x\":342.0,\"center_y\":117.0,\"width\":400.0,\"height\":250.0,\"text\":\"int x\",\"output_knot\":{\"id\":\"873937fc-3476-498d-8bf8-4b485b139091\"},\"id\":\"2d7b4c40-224d-45be-9039-d7d4a7039d2e\"},{\"type\":\"ConditionType\",\"center_x\":344.47134,\"center_y\":516.35034,\"width\":400.0,\"height\":250.0,\"text\":\"x > 10\",\"false_knot\":{\"id\":\"be237c23-c70e-440b-8868-9c786e4d367c\"},\"true_knot\":{\"id\":\"f636896c-58ab-4e44-89d7-9ac3fe26c3cc\"},\"top_knot\":{\"id\":\"e6b92814-6ec2-40b0-ace6-f284228b01ce\"},\"bottom_knot\":{\"id\":\"036a71af-9d00-426a-b691-21cb0345a8fd\"},\"id\":\"8a5062cb-9571-41e6-948f-4f667112833f\"},{\"type\":\"CalculationType\",\"center_x\":-45.451385,\"center_y\":814.3176,\"width\":400.0,\"height\":250.0,\"text\":\"y = 10\",\"left_knot\":{\"id\":\"5c2a6923-5e39-47a1-b7ed-8b44774a1f88\"},\"right_knot\":{\"id\":\"dac4cf01-c762-4a50-8f40-7784756a3a64\"},\"top_knot\":{\"id\":\"24722649-70e7-4802-a182-7686ded390cb\"},\"bottom_knot\":{\"id\":\"6b6f33cb-c65b-4931-97f4-f5c4b7812d97\"},\"id\":\"a0e0396a-5a91-4e2c-beec-93c7dd556301\"},{\"type\":\"CalculationType\",\"center_x\":695.6665,\"center_y\":815.74884,\"width\":400.0,\"height\":250.0,\"text\":\"g = sum(x,5)\",\"left_knot\":{\"id\":\"7ccfc52a-08e0-4654-9d08-be6cbac18aa3\"},\"right_knot\":{\"id\":\"63be9d9a-f3e4-4eee-b9e2-7c57aa140212\"},\"top_knot\":{\"id\":\"994f1058-ca47-4bda-9de2-f84bbdbb7093\"},\"bottom_knot\":{\"id\":\"1a13549a-2d16-4fd2-b1a7-66682633cb64\"},\"id\":\"2a6744ab-6d34-405d-bb6f-cb13cfcf9802\"},{\"type\":\"EndType\",\"center_x\":326.73956,\"center_y\":1588.1763,\"width\":400.0,\"height\":250.0,\"text\":\"x\",\"input_knot\":{\"id\":\"a5aca9d9-af0e-4706-8a43-b614f5cd46ae\"},\"id\":\"0dd5a2e7-081d-4c25-9fc9-bdf42ae14cf5\"}],\"connections\":[{\"start_knot_id\":\"873937fc-3476-498d-8bf8-4b485b139091\",\"start_block_id\":\"2d7b4c40-224d-45be-9039-d7d4a7039d2e\",\"end_knot_id\":\"e6b92814-6ec2-40b0-ace6-f284228b01ce\",\"end_block_id\":\"8a5062cb-9571-41e6-948f-4f667112833f\"},{\"start_knot_id\":\"be237c23-c70e-440b-8868-9c786e4d367c\",\"start_block_id\":\"8a5062cb-9571-41e6-948f-4f667112833f\",\"end_knot_id\":\"24722649-70e7-4802-a182-7686ded390cb\",\"end_block_id\":\"a0e0396a-5a91-4e2c-beec-93c7dd556301\"},{\"start_knot_id\":\"f636896c-58ab-4e44-89d7-9ac3fe26c3cc\",\"start_block_id\":\"8a5062cb-9571-41e6-948f-4f667112833f\",\"end_knot_id\":\"994f1058-ca47-4bda-9de2-f84bbdbb7093\",\"end_block_id\":\"2a6744ab-6d34-405d-bb6f-cb13cfcf9802\"},{\"start_knot_id\":\"6b6f33cb-c65b-4931-97f4-f5c4b7812d97\",\"start_block_id\":\"a0e0396a-5a91-4e2c-beec-93c7dd556301\",\"end_knot_id\":\"a5aca9d9-af0e-4706-8a43-b614f5cd46ae\",\"end_block_id\":\"0dd5a2e7-081d-4c25-9fc9-bdf42ae14cf5\"},{\"start_knot_id\":\"1a13549a-2d16-4fd2-b1a7-66682633cb64\",\"start_block_id\":\"2a6744ab-6d34-405d-bb6f-cb13cfcf9802\",\"end_knot_id\":\"a5aca9d9-af0e-4706-8a43-b614f5cd46ae\",\"end_block_id\":\"0dd5a2e7-081d-4c25-9fc9-bdf42ae14cf5\"}]}"

        editor.loadConfigurations(saveData)
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
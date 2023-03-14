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
        val saveData = "{\"blocks\":[{\"type\":\"CalculationType\",\"center_x\":351.0,\"center_y\":1103.0,\"width\":400.0,\"height\":250.0,\"text\":\"x = sum(x, 8)\",\"left_knot\":{\"id\":\"23f270d5-b023-477d-bb4e-9fd6a1a41f31\"},\"right_knot\":{\"id\":\"fc5d0ee7-3dcc-4385-a925-38a40161359d\"},\"top_knot\":{\"id\":\"3b669fa5-93d2-43e3-8d84-94dfcdfed9bb\"},\"bottom_knot\":{\"id\":\"3e53d345-02bf-4321-80a2-3235a30c4bc4\"},\"id\":\"d03d4464-8b45-47eb-be13-8465c58a2450\"},{\"type\":\"CalculationType\",\"center_x\":1166.0059,\"center_y\":1102.001,\"width\":400.0,\"height\":250.0,\"text\":\"x = sum(x, 9)\",\"left_knot\":{\"id\":\"b6ae30dd-9eda-4141-a5dd-bb86a9375311\"},\"right_knot\":{\"id\":\"2567dabd-0dc5-45a5-b7cb-9cde56e22831\"},\"top_knot\":{\"id\":\"347481bb-f55a-4d8a-b6d1-b3234a7ed746\"},\"bottom_knot\":{\"id\":\"541465bc-a41b-4593-a45c-0ae6237e5761\"},\"id\":\"18fef1a1-d463-4309-8c06-dede8eb15e35\"},{\"type\":\"ConditionType\",\"center_x\":728.0,\"center_y\":741.0,\"width\":400.0,\"height\":250.0,\"text\":\"x > 7\",\"false_knot\":{\"id\":\"b4eb9778-25ee-416c-a289-16c558de581a\"},\"true_knot\":{\"id\":\"1495be32-9bff-49f0-97d8-777c61f17846\"},\"top_knot\":{\"id\":\"7a78d9cb-77f9-4288-96ab-07c8741a2aab\"},\"bottom_knot\":{\"id\":\"6bce764d-6e07-4e26-af6b-9e02739a008c\"},\"id\":\"337e8e97-014d-4fa6-9b73-3e3816030a48\"},{\"type\":\"EndType\",\"center_x\":741.0,\"center_y\":1551.0,\"width\":400.0,\"height\":250.0,\"text\":\"x\",\"input_knot\":{\"id\":\"1df16b2c-7840-43ae-84ae-71e4975a2742\"},\"id\":\"860bcdcf-7bd2-4e2f-bc4f-d29d00c0e525\"},{\"type\":\"StartType\",\"center_x\":728.0,\"center_y\":310.0,\"width\":400.0,\"height\":250.0,\"text\":\"int x\",\"output_knot\":{\"id\":\"49f07000-c47e-4f83-b13f-881acbadc2e8\"},\"id\":\"57bd0386-bf07-48b7-b2e7-704e6cb703c6\"}],\"connections\":[{\"start_knot_id\":\"541465bc-a41b-4593-a45c-0ae6237e5761\",\"start_block_id\":\"18fef1a1-d463-4309-8c06-dede8eb15e35\",\"end_knot_id\":\"1df16b2c-7840-43ae-84ae-71e4975a2742\",\"end_block_id\":\"860bcdcf-7bd2-4e2f-bc4f-d29d00c0e525\"},{\"start_knot_id\":\"1495be32-9bff-49f0-97d8-777c61f17846\",\"start_block_id\":\"337e8e97-014d-4fa6-9b73-3e3816030a48\",\"end_knot_id\":\"347481bb-f55a-4d8a-b6d1-b3234a7ed746\",\"end_block_id\":\"18fef1a1-d463-4309-8c06-dede8eb15e35\"},{\"start_knot_id\":\"b4eb9778-25ee-416c-a289-16c558de581a\",\"start_block_id\":\"337e8e97-014d-4fa6-9b73-3e3816030a48\",\"end_knot_id\":\"3b669fa5-93d2-43e3-8d84-94dfcdfed9bb\",\"end_block_id\":\"d03d4464-8b45-47eb-be13-8465c58a2450\"},{\"start_knot_id\":\"3e53d345-02bf-4321-80a2-3235a30c4bc4\",\"start_block_id\":\"d03d4464-8b45-47eb-be13-8465c58a2450\",\"end_knot_id\":\"1df16b2c-7840-43ae-84ae-71e4975a2742\",\"end_block_id\":\"860bcdcf-7bd2-4e2f-bc4f-d29d00c0e525\"},{\"start_knot_id\":\"49f07000-c47e-4f83-b13f-881acbadc2e8\",\"start_block_id\":\"57bd0386-bf07-48b7-b2e7-704e6cb703c6\",\"end_knot_id\":\"7a78d9cb-77f9-4288-96ab-07c8741a2aab\",\"end_block_id\":\"337e8e97-014d-4fa6-9b73-3e3816030a48\"}]}"

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
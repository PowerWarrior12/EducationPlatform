package com.example.educationplatform.presentation.courseEdit.structure

import android.graphics.Color
import android.view.View
import android.view.View.OnClickListener
import com.example.educationplatform.databinding.TextEditorMenuBinding
import jp.wasabeef.richeditor.RichEditor


class TextEditorMenu(private val menuBinding: TextEditorMenuBinding, private val editor: RichEditor) {
    fun init() {
        menuBinding.apply {
            actionUndo.setOnClickListener {
                editor.undo()
            }
            actionRedo.setOnClickListener {
                editor.redo()
            }
            actionBold.setOnClickListener {
                editor.setBold()
            }
            actionItalic.setOnClickListener {
                editor.setItalic()
            }
            actionSubscript.setOnClickListener {
                editor.setSubscript()
            }
            actionSuperscript.setOnClickListener {
                editor.setSuperscript()
            }
            actionStrikethrough.setOnClickListener {
                editor.setStrikeThrough()
            }
            actionUnderline.setOnClickListener {
                editor.setUnderline()
            }
            actionHeading1.setOnClickListener {
                editor.setHeading(1)
            }
            actionHeading2.setOnClickListener {
                editor.setHeading(2)
            }
            actionHeading3.setOnClickListener {
                editor.setHeading(3)
            }
            actionHeading4.setOnClickListener {
                editor.setHeading(4)
            }
            actionHeading5.setOnClickListener {
                editor.setHeading(5)
            }
            actionHeading6.setOnClickListener {
                editor.setHeading(6)
            }
            actionTxtColor.setOnClickListener(object: OnClickListener {
                private var isChanged = false
                override fun onClick(p0: View?) {
                    if (isChanged) {
                        editor.setTextColor(Color.BLACK)
                    } else {
                        editor.setTextColor(Color.RED)
                    }
                    isChanged = !isChanged
                }

            })
            actionTxtColor.setOnClickListener(object: OnClickListener {
                private var isChanged = false
                override fun onClick(p0: View?) {
                    if (isChanged) {
                        editor.setTextBackgroundColor(Color.BLACK)
                    } else {
                        editor.setTextBackgroundColor(Color.RED)
                    }
                    isChanged = !isChanged
                }
            })
            actionIndent.setOnClickListener {
                editor.setIndent()
            }
            actionOutdent.setOnClickListener {
                editor.setOutdent()
            }
            actionAlignCenter.setOnClickListener {
                editor.setAlignCenter()
            }
            actionAlignLeft.setOnClickListener {
                editor.setAlignLeft()
            }
            actionAlignRight.setOnClickListener {
                editor.setAlignRight()
            }
            actionBlockquote.setOnClickListener {
                editor.setBlockquote()
            }
            actionInsertBullets.setOnClickListener {
                editor.setBullets()
            }
            actionInsertNumbers.setOnClickListener {
                editor.setNumbers()
            }
            actionInsertLink.setOnClickListener {
                editor.insertLink("https://github.com/wasabeef", "wasabeef")
            }
            actionInsertCheckbox.setOnClickListener {
                editor.insertTodo()
            }
            actionInsertImage.setOnClickListener {
                editor.insertImage("https://i.postimg.cc/Y2Z5vQsg/java-1.png",
                    "", 320)
            }
        }
    }
}
package com.example.educationplatform.presentation.cource.modules.adapter

import com.example.educationplatform.databinding.ModuleItemBinding
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun takesModuleAdapter(onModuleClick: (moduleId: Int) -> Unit) =
    adapterDelegateViewBinding<ModuleItem.TakesModuleItem, ModuleItem, ModuleItemBinding>(
        { layoutInflater, root -> ModuleItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.apply {
                binding.root.isEnabled = item.checkable

                val alpha = if (item.checkable) { 1.0f } else { 0.5f }

                titleText.alpha = alpha
                informationText.alpha = alpha
                scoreText.alpha = alpha

                titleText.text = item.module.title
                informationText.text = item.module.info
                scoreText.text = item.module.getFormatScore()
                root.setOnClickListener {
                    onModuleClick(item.module.id)
                }
            }
        }
    }
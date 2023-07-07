package com.example.educationplatform.presentation.home.horizontalAdapter

import com.bumptech.glide.Glide
import com.example.educationplatform.R
import com.example.educationplatform.databinding.EditCourseItemBinding
import com.example.educationplatform.domain.entities.UsersCourse
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun editCoursesAdapterDelegate(onEditCourseClick: (courseId: Int) -> Unit) =
    adapterDelegateViewBinding<UsersCourse, UsersCourse, EditCourseItemBinding>(
        { layoutInflater, root -> EditCourseItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.root.setOnClickListener {
                onEditCourseClick(item.id)
            }
            binding.apply {
                courseTitle.text = item.title
                Glide
                    .with(binding.root)
                    .load(item.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.student)
                    .into(binding.imageView)
            }
        }
    }
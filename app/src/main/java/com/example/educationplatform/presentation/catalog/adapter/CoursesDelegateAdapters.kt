package com.example.educationplatform.presentation.catalog.adapter

import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.educationplatform.R
import com.example.educationplatform.databinding.CourseItemBinding
import com.example.educationplatform.utils.extensions.setDefaultSkeletonDrawable
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun coursesAdapterDelegate(onClickListener: (courseId: Int) -> Unit) =
    adapterDelegateViewBinding<CourseItem.SimpleCourseItem, CourseItem, CourseItemBinding>(
        { layoutInflater, root -> CourseItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.root.setOnClickListener {
                onClickListener(item.course.id)
            }
            binding.apply {
                courseTitle.text = item.course.title
                informationText.text = item.course.info
                Glide
                    .with(binding.root)
                    .load(item.course.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.student)
                    .into(binding.imageView)
                rating.text = "${item.course.rating}"
                usersCount.text = "${item.course.usersCount}"
            }
        }
    }

fun loadingItemAdapterDelegate() =
    adapterDelegateViewBinding<CourseItem.LoadingItem, CourseItem, CourseItemBinding>(
        { layoutInflater, root -> CourseItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.apply {
                binding.apply {
                    starIcon.isVisible = false
                    watchIcon.isVisible = false
                    usersCount.isVisible = false
                    rating.isVisible = false
                }

                setDefaultSkeletonDrawable(root, listOf(R.id.watch_icon, R.id.star_icon, R.id.users_count, R.id.rating))
            }
        }
    }
package com.example.educationplatform.presentation.home.mainAdapter

import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.educationplatform.R
import com.example.educationplatform.databinding.ButtonItemBinding
import com.example.educationplatform.databinding.HorizontalRecycleItemBinding
import com.example.educationplatform.databinding.TakesCourseItemBinding
import com.example.educationplatform.databinding.TitleItemBinding
import com.example.educationplatform.presentation.home.horizontalAdapter.EditCoursesAdapter
import com.example.educationplatform.utils.decorators.MarginDecorator
import com.example.educationplatform.utils.extensions.setDefaultSkeletonDrawable
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun titleAdapterDelegate() =
    adapterDelegateViewBinding<UsersCourseItem.TitleItem, UsersCourseItem, TitleItemBinding>(
        { layoutInflater, root -> TitleItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.root.text = item.title
        }
    }

fun buttonAdapterDelegate(clickListener: View.OnClickListener) =
    adapterDelegateViewBinding<UsersCourseItem.ButtonItem, UsersCourseItem, ButtonItemBinding>(
        { layoutInflater, root -> ButtonItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.button.apply {
                text = resources.getString(R.string.create_course_button)
                setOnClickListener(clickListener)
            }
        }
    }

fun takesCoursesAdapterDelegate(onTakesCourseClick: (courseId: Int) -> Unit) =
    adapterDelegateViewBinding<UsersCourseItem.TakesCourseItem, UsersCourseItem, TakesCourseItemBinding>(
        { layoutInflater, root -> TakesCourseItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.root.setOnClickListener {
                onTakesCourseClick(item.subCourse.id)
            }
            binding.apply {
                courseTitle.text = item.subCourse.title
                scoreText.text = item.subCourse.getScoreFormat()
                Glide
                    .with(binding.root)
                    .load(item.subCourse.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.student)
                    .into(binding.imageView)
            }
        }
    }

fun horizontalItemAdapterDelegate(onEditCourseClick: (courseId: Int) -> Unit) =
    adapterDelegateViewBinding<UsersCourseItem.HorizontalItems, UsersCourseItem, HorizontalRecycleItemBinding>(
        { layoutInflater, root -> HorizontalRecycleItemBinding.inflate(layoutInflater, root, false) }
    ) {
        val adapter = EditCoursesAdapter(onEditCourseClick)
        binding.root.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
            binding.root.adapter = adapter
            addItemDecoration(MarginDecorator(listOf(0), left = 60, right = 60, top = 30, bottom = 30))
        }
        bind {
            adapter.items = item.userCourses
            binding.root.restoreState(item.state)
        }
        onViewDetachedFromWindow {
            item.state = binding.root.layoutManager?.onSaveInstanceState()
        }
    }

fun loadingItemAdapterDelegate() =
    adapterDelegateViewBinding<UsersCourseItem.LoadingItem, UsersCourseItem, TakesCourseItemBinding>(
        { layoutInflater, root -> TakesCourseItemBinding.inflate(layoutInflater, root, false) }
    ) {
        setDefaultSkeletonDrawable(binding.root, listOf(R.id.continue_button))
        bind {
            binding.continueButton.visibility = View.INVISIBLE
        }
    }

private fun RecyclerView.restoreState(parcelable: Parcelable?) {
    if (parcelable == null) return
    layoutManager?.onRestoreInstanceState(parcelable)
}
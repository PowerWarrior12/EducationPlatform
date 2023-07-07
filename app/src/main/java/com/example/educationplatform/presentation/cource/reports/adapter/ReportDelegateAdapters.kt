package com.example.educationplatform.presentation.cource.reports.adapter

import android.view.View
import com.bumptech.glide.Glide
import com.example.educationplatform.R
import com.example.educationplatform.databinding.ReportItemBinding
import com.example.educationplatform.databinding.SendReportItemBinding
import com.example.educationplatform.utils.TextWatcherImpl
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun sendReportAdapter(onSendClick: (message: String, rating: Int) -> Unit) =
    adapterDelegateViewBinding<ReportItem.AddReportItem, ReportItem, SendReportItemBinding>(
        { layoutInflater, root -> SendReportItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            Glide
                .with(binding.root)
                .load(item.user.icon)
                .centerCrop()
                .placeholder(R.drawable.student)
                .into(binding.icon)

            binding.editReportText.addTextChangedListener(object: TextWatcherImpl() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0 == "") {
                        binding.sendButton.visibility = View.GONE
                    } else {
                        binding.sendButton.visibility = View.VISIBLE
                    }
                }
            })
            binding.sendButton.setOnClickListener {
                binding.apply {
                    onSendClick.invoke(editReportText.text.toString(), ratingBar.rating.toInt())
                    editReportText.setText("")
                }
            }
        }
    }

fun userReportAdapter() =
    adapterDelegateViewBinding<ReportItem.UserReportItem, ReportItem, ReportItemBinding>(
        { layoutInflater, root -> ReportItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.apply {
                Glide
                    .with(root)
                    .load(item.report.userIcon)
                    .centerCrop()
                    .placeholder(R.drawable.student)
                    .into(icon)
                userName.text = item.report.userName
                message.text = item.report.text
                rating.text = item.report.rating.toString()
            }
        }
    }
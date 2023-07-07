package com.example.educationplatform.utils

import com.example.educationplatform.data.local.user.entities.UserEntity
import com.example.educationplatform.data.remote.report.entities.ReportResponse

object TestUtil {
    fun createUserEntity(
        id: Int = 1,
        name: String = "name",
        secondName: String = "second_name",
        icon: String = "",
        email: String = "email",
        info: String = "info",
        status: String = "status"
    ) =
        UserEntity(
            id = id,
            name = name,
            secondName = secondName,
            icon = icon,
            email = email,
            info = info,
            status = status
        )

    fun createReportResponse(
        text: String = "text",
        rating: Int = 1,
        date: String = "30-12-2011",
        userName: String = "user_name"
    ) = ReportResponse(
        text = text,
        rating = rating,
        date = date,
        userName = userName
    )
}
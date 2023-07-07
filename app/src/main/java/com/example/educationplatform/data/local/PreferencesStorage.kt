package com.example.educationplatform.data.local

import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject

private const val SHARED_PREFERENCES_NAME = "education_application"
private const val STATUS_NAME = "status"

class PreferencesStorage @Inject constructor(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun saveStatus(status: String) {
        sharedPreferences.edit {
            putString(STATUS_NAME, status)
        }
    }

    fun getStatus(): String? {
        return sharedPreferences.getString(STATUS_NAME, null)
    }
}
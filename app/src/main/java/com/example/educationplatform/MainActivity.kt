package com.example.educationplatform

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.example.educationplatform.presentation.authorization.AuthorizationFragment

class MainActivity : AppCompatActivity(R.layout.main_activity) {
    @SuppressLint("CommitTransaction")
    override fun onStart() {
        super.onStart()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, AuthorizationFragment())
            .commitNow()
    }
}
package com.example.doanchuyende

import android.app.Application
import com.google.firebase.FirebaseApp

class DoAnChuyenDeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
} 
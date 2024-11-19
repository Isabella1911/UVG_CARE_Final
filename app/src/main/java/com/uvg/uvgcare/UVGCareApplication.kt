package com.uvg.uvgcare
import android.app.Application
import com.google.firebase.FirebaseApp
class UVGCareApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
package com.example.gaminghub.framework
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NachoApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
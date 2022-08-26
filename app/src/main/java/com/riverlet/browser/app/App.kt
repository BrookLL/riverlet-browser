package com.riverlet.browser.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log

private const val TAG = "App"

class App : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context
        fun getAppContext() = context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        Log.d(TAG, "onCreate:${this}")
    }
}
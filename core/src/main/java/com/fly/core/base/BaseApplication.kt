package com.fly.core.base

import android.app.Application

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}

lateinit var appContext: Application
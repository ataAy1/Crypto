package com.example.koink

import android.app.Application
import com.example.koink.di.anotherModule
import com.example.koink.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@MyApplication)
            modules(appModule, anotherModule)
        }
    }
}
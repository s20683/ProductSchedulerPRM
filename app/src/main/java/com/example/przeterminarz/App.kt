package com.example.przeterminarz

import android.app.Application
import com.example.przeterminarz.data.RepositoryLocator

class App :Application() {

    override fun onCreate() {
        super.onCreate()
        RepositoryLocator.init(applicationContext)
    }
}
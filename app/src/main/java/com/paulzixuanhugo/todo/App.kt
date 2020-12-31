package com.paulzixuanhugo.todo

import android.app.Application
import android.content.Intent
import androidx.preference.PreferenceManager
import com.paulzixuanhugo.todo.network.Api

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Api.INSTANCE = Api(this)
    }
}
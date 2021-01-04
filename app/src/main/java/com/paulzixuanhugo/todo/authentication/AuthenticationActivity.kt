package com.paulzixuanhugo.todo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager


class AuthenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        val token = PreferenceManager.getDefaultSharedPreferences(this).getString(SHARED_PREF_TOKEN_KEY, "")
        if (token != null && token != "")
        {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
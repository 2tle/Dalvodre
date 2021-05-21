package com.example.today_kotlin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler = Handler(Looper.myLooper()!!)
        handler.postDelayed({
            val intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

}
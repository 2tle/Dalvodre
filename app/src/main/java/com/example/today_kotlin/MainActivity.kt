package com.example.today_kotlin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler = Handler(Looper.myLooper()!!)
        val dateAndtime: LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH")
        var formatted = dateAndtime.format(formatter)
        var background = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.background)

        if(formatted.toInt()>=5 && formatted.toInt()<=18)
            background.setBackgroundResource(R.drawable.not)
        else if(formatted.toInt()>=16 && formatted.toInt()<=21)
            background.setBackgroundResource(R.drawable.dinner)

        handler.postDelayed({
            val intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)

    }
}
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler = Handler(Looper.myLooper()!!)
        val dateAndtime: LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH")
        val formatted = dateAndtime.format(formatter)
        val background = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.background)

        if(formatted.toInt() in 5..18)
            background.setBackgroundResource(R.drawable.not)
        else if(formatted.toInt() in 16..21)
            background.setBackgroundResource(R.drawable.dinner)

        handler.postDelayed({
            val intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)

    }
}
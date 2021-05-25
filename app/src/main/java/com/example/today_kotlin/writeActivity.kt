package com.example.today_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class writeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        val dateAndtime: LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH")
        var formatted = dateAndtime.format(formatter)
        var background = findViewById<TextView>(R.id.write_word)

        if(formatted.toInt()>=5 && formatted.toInt()<=18)
            background.setBackgroundResource(R.drawable.not)
        else if(formatted.toInt()>=16 && formatted.toInt()<=21)
            background.setBackgroundResource(R.drawable.dinner)
    }
}
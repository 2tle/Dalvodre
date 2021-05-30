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
        val formatted = dateAndtime.format(formatter)
        val background = findViewById<TextView>(R.id.write_word)

        if(formatted.toInt() in 5..18)
            background.setBackgroundResource(R.drawable.not)
        else if(formatted.toInt() in 16..21)
            background.setBackgroundResource(R.drawable.dinner)
    }
}
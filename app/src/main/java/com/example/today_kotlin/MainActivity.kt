package com.example.today_kotlin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler = Handler(Looper.myLooper()!!)
        val dateTime: LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH")
        val formatted = dateTime.format(formatter)
        val background : androidx.constraintlayout.widget.ConstraintLayout = findViewById(R.id.background) //여기까지 선언

        if(formatted.toInt() in 5..15)
            background.setBackgroundResource(R.drawable.not)
        if(formatted.toInt() in 16..21)
            background.setBackgroundResource(R.drawable.dinner) //시간에 따라 테마 변경

        handler.postDelayed({
            val intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) //화면이 잠깐 보이게 설정

    }
}
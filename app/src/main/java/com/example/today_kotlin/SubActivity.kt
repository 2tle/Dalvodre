package com.example.today_kotlin

import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.finishAffinity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.system.exitProcess

class SubActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var backKeyPressedTime : Long = 0;
    override fun onBackPressed() {
        lateinit var toast: Toast
        if(System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis()
            toast = Toast.makeText(this,"뒤로가기를 한번 더 누르면 종료됩니다.",Toast.LENGTH_LONG)
            toast.show()
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finishAffinity()
            finish()
            System.runFinalization()
            moveTaskToBack(true)
            finishAndRemoveTask()
            exitProcess(0)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)

        val subBtn = findViewById<Button>(R.id.subBtn)
        subBtn.setOnClickListener {
            if(auth.currentUser != null) {
                startActivity(Intent(this, Main2Activity::class.java))
            }
        }






    }


}
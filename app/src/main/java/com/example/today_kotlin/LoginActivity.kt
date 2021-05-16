package com.example.today_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        auth = Firebase.auth
        var loginBtn = findViewById(R.id.loginBtn) as Button
        var regBtn = findViewById(R.id.register) as Button
        var email = findViewById(R.id.email) as EditText
        var pw = findViewById(R.id.pw) as EditText
        loginBtn.setOnClickListener {
            signIn(email.text.toString(),pw.text.toString())
        }
        regBtn.setOnClickListener {
            val nextIntent = Intent(this, RegisterActivity::class.java)
            startActivity(nextIntent)
        }

    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null) { /* reload(); */ }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(baseContext, "로그인 성공",
                            Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                    } else {
                        Toast.makeText(baseContext, "로그인 실패, 다시 시도해주세요",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }

}
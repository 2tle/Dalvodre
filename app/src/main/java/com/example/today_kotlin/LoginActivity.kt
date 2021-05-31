package com.example.today_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        auth = Firebase.auth
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val regBtn = findViewById<Button>(R.id.register)
        val email = findViewById<EditText>(R.id.email)
        val pw = findViewById<EditText>(R.id.pw)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("로그인 오류")
        builder.setMessage("아이디 혹은 비밀번호를 확인해주세요.")
        builder.setPositiveButton("확인", null)
        loginBtn.setOnClickListener {
            if(email.text.toString() == "" || pw.text.toString() == "") {
                builder.show()
            }else {signIn(email.text.toString(),pw.text.toString())}
        }
        regBtn.setOnClickListener {
            val nextIntent = Intent(this, RegisterActivity::class.java)
            startActivity(nextIntent)
        }

    }
    override fun onStart() {
        super.onStart()
        var currentUser = auth.currentUser
        currentUser = null
        if(currentUser != null) {
            startActivity(Intent(this, subActivity::class.java))
        }
    }

    private fun signIn(email: String, password: String) {
        val builder = AlertDialog.Builder(this)
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        startActivity(Intent(this, subActivity::class.java))
                    } else {
                        builder.setTitle("로그인 오류")
                        builder.setMessage("아이디 혹은 비밀번호를 확인해주세요.")
                        builder.setPositiveButton("확인", null)
                        builder.show()
                    }
                }
    }

}
package com.example.today_kotlin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
        var loginBtn = findViewById(R.id.loginBtn) as Button
        var regBtn = findViewById(R.id.register) as Button
        var email = findViewById(R.id.email) as EditText
        var pw = findViewById(R.id.pw) as EditText
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
        val currentUser = auth.currentUser
        if(currentUser != null) {
            //메인씬 연결
            //startActivity(Intent(this, 메인화면액티비티::class.java))
        }
    }

    private fun signIn(email: String, password: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("로그인 오류")
        builder.setMessage("아이디 혹은 비밀번호를 확인해주세요.")
        builder.setPositiveButton("확인", null)
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        //startActivity(Intent(this, 메인화면액티비티::class.java))
                    } else {
                        builder.setTitle("로그인 오류")
                        builder.setMessage("아이디 혹은 비밀번호를 확인해주세요.")
                        builder.setPositiveButton("확인", null)
                        builder.show()
                    }
                }
    }

}
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
        val pw = findViewById<EditText>(R.id.pw) //여기까지 선언

        loginBtn.setOnClickListener {
            if(email.text.toString() == "" || pw.text.toString() == "") {
                showAlertDialog("로그인 오류","이메일 혹은 비밀번호를 입력해주세요.")
            }else {signIn(email.text.toString(),pw.text.toString())}
        }
        regBtn.setOnClickListener {
            val nextIntent = Intent(this, RegisterActivity::class.java)
            startActivity(nextIntent)
        }

    }

    private fun showAlertDialog(title:String, message:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("확인", null)
        builder.show()
    }


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null) {
            startActivity(Intent(this, SubActivity::class.java))
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, SubActivity::class.java))
                } else {
                    showAlertDialog("로그인 오류","서버상의 문제가 발생했습니다. 나중에 다시 시도해주세요.")
                }
            }.addOnFailureListener {
                showAlertDialog("로그인 오류","서버상의 문제가 발생했습니다. 나중에 다시 시도해주세요.")
            }
    }

}
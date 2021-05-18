package com.example.today_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.today_kotlin.LoginActivity as LoginActivity

class RegisterActivity : AppCompatActivity() {
    private var auth: FirebaseAuth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val user = Firebase.auth.currentUser
        if(user != null) {
            Toast.makeText(baseContext, "로그인이 이미 되어있습니다.", //메인으로 넘어가기
                Toast.LENGTH_SHORT).show()
        }
        var email = findViewById(R.id.email) as EditText
        var pw = findViewById(R.id.pw) as EditText
        var name = findViewById(R.id.username) as EditText
        var regBtn = findViewById(R.id.regBtn) as Button

        regBtn.setOnClickListener {

            createAccount(email.text.toString(),pw.text.toString(), name.text.toString())
            //writeNewUserWithTaskListeners()

        }


    }

    private fun createAccount(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    createFireStoreUsername(user.uid,name,user.email)

                } else {
                    Toast.makeText(baseContext, "계정생성 실패, 다시 시도해주세요.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }


    fun createFireStoreUsername(userId: String,name: String,email: String) {
        val db = Firebase.firestore
        val user = hashMapOf(
            "userId" to userId,
            "email" to email,
            "name" to name
        )
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                val nextIntent = Intent(this, LoginActivity::class.java)
                startActivity(nextIntent)

            }
            .addOnFailureListener { e ->
                Toast.makeText(baseContext, "계정생성 실패, 다시 시도해주세요.",
                    Toast.LENGTH_SHORT).show()
            }
    }






}
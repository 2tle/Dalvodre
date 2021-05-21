package com.example.today_kotlin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.today_kotlin.LoginActivity as LoginActivity

class RegisterActivity : AppCompatActivity() {
    private var auth: FirebaseAuth = Firebase.auth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val user = Firebase.auth.currentUser
        var email = findViewById(R.id.email) as EditText
        var pw = findViewById(R.id.pw) as EditText
        var name = findViewById(R.id.username) as EditText
        var regBtn = findViewById(R.id.regBtn) as Button

        regBtn.setOnClickListener {

            createAccount(email.text.toString(),pw.text.toString(), name.text.toString())

        }


    }

    private fun createAccount(email: String, password: String, name: String) {
        val builder = AlertDialog.Builder(this)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    //createFireStoreUsername(user.uid,name,user.email)
                    val profileCreateOrUpdates = userProfileChangeRequest {
                        displayName=name
                        photoUri=Uri.parse("https://firebasestorage.googleapis.com/v0/b/today-kotlin.appspot.com/o/profile-project.png?alt=media&token=336f24a4-ba3c-4be7-9ef2-a9f24a50db35");
                    }
                    user!!.updateProfile(profileCreateOrUpdates)
                        .addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            startActivity(Intent(this, subActivity::class.java))
                        } else {
                            builder.setTitle("회원가입 오류")
                            builder.setMessage("회원가입에 실패하였습니다. 관리자에게 문의하세요.")
                            builder.setPositiveButton("확인", null)
                            builder.show()
                        }
                    }

                } else {
                    builder.setTitle("회원가입 오류")
                    builder.setMessage("회원가입에 실패하였습니다. 관리자에게 문의하세요.")
                    builder.setPositiveButton("확인", null)
                    builder.show()
                }
            }
    }


    /* fun createFireStoreUsername(userId: String,name: String,email: String) {
        val builder = AlertDialog.Builder(this)
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
                builder.setTitle("회원가입 오류")
                builder.setMessage("회원가입에 실패하였습니다. 관리자에게 문의하세요.")
                builder.setPositiveButton("확인", null)
                builder.show()
            }
    } */






}
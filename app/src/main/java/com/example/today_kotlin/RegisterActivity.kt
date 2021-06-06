package com.example.today_kotlin

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import com.example.today_kotlin.LoginActivity as LoginActivity

@Suppress("UNCHECKED_CAST", "NAME_SHADOWING")
class RegisterActivity : AppCompatActivity() {
    private var auth: FirebaseAuth = Firebase.auth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val email = findViewById<EditText>(R.id.email)
        val pw = findViewById<EditText>(R.id.pw)
        val pwCheck = findViewById<EditText>(R.id.pwch);
        val name = findViewById<EditText>(R.id.username)
        val regBtn = findViewById<Button>(R.id.regBtn)
        val ccBtn = findViewById<Button>(R.id.ccBtn)

        ccBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("회원가입 오류")
        builder.setMessage("형식에 맞게 입력해주세요.")
        builder.setPositiveButton("확인", null)
        regBtn.setOnClickListener {
            if(pw.text.toString() != "" && pw.text.toString() == pwCheck.text.toString() && email.text.toString() != "" && name.text.toString() != "")
                createAccount(email.text.toString(),pw.text.toString(), name.text.toString())
            else {
                builder.show()
            }

        }


    }

    private fun createAccount(email: String, password: String, name: String) {
        val db = Firebase.firestore
        val builder = AlertDialog.Builder(this)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileCreateOrUpdates = userProfileChangeRequest {
                        displayName=name
                        photoUri=Uri.parse("https://firebasestorage.googleapis.com/v0/b/today-kotlin.appspot.com/o/profile0.png?alt=media&token=1aee06c2-42d3-4700-b72c-8e2bc71227f1");
                    }
                    user!!.updateProfile(profileCreateOrUpdates)
                        .addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            val docu = db.collection("words").document("mGHEB2dhXFQkPF8KJww2")
                            docu.get().addOnSuccessListener { document ->
                                val wordList: ArrayList<String> = document.data?.get("words") as ArrayList<String>
                                val itemList: ArrayList<String> = document.data?.get("itemName") as ArrayList<String>
                                val itemSrc: ArrayList<String> = document.data?.get("itemSrc") as ArrayList<String>
                                val itemIdx = Random().nextInt(itemList.size)
                                val todayItem: String = itemList.get(itemIdx)
                                val todayItemSrc: String = itemSrc.get(itemIdx)
                                val todayWords: String = wordList.get(Random().nextInt(wordList.size))
                                val listWords: ArrayList<String> = arrayListOf()
                                val listDates: ArrayList<String> = arrayListOf()

                                listWords.add(todayWords)
                                listDates.add(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE).toString())
                                val firestoreData = hashMapOf(
                                    "date" to LocalDateTime.now().format(DateTimeFormatter.ISO_DATE),
                                    "todayWords" to todayWords,
                                    "listWords" to listWords,
                                    "listDates" to listDates,
                                    "item" to todayItem,
                                    "itemSrc" to todayItemSrc
                                )

                                db.collection("users").document(user.uid).set(firestoreData).addOnSuccessListener {
                                    builder.setTitle("회원가입 성공")
                                    builder.setMessage("회원가입에 성공하였습니다. 감사합니다.")
                                    builder.setPositiveButton("확인"){ _: DialogInterface, _: Int ->
                                        startActivity(Intent(this, subActivity::class.java))
                                    }
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

                } else {
                    builder.setTitle("회원가입 오류")
                    builder.setMessage("회원가입에 실패하였습니다. 관리자에게 문의하세요.")
                    builder.setPositiveButton("확인", null)
                    builder.show()
                }
            }
    }
}
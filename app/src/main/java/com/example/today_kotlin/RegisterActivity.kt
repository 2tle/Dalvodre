package com.example.today_kotlin

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern
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
        val pwCheck = findViewById<EditText>(R.id.pwch)
        val name = findViewById<EditText>(R.id.username)
        val regBtn = findViewById<Button>(R.id.regBtn)
        val ccBtn = findViewById<Button>(R.id.ccBtn)
        val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        val pwRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#\$%^&*()+|=])[A-Za-z\\d~!@#\$%^&*()+|=]{8,16}"
        var check1 = false
        var check2 = false
        var check3 = false
        var check4 = false
        val dateTime: LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH")
        val formatted = dateTime.format(formatter)
        val background : LinearLayout = findViewById(R.id.registerBack) //여기까지 선언

        if(formatted.toInt() in 5..16)
            background.setBackgroundResource(R.drawable.not)
        if(formatted.toInt() in 16..21)
            background.setBackgroundResource(R.drawable.dinner) //시간에 따라 테마 변경

        ccBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        } //취소 버튼 눌렀을 때 다시 로그인 화면으로 돌아가기


        fun checkEmail() : Boolean {
            return Pattern.matches(emailValidation, email.text.toString().trim())
        }

        fun checkPw() : Boolean {
            return Pattern.matches(pwRegex, pw.text.toString())
        }

        fun checkPwCheck(): Boolean {
            return Pattern.matches(pwRegex, pwCheck.text.toString()) && (pw.text.toString() == pwCheck.text.toString())
        }

        fun checkName(): Boolean {
            return name.text.toString().length <= 8 && name.text.toString() != ""
        }
        regBtn.setOnClickListener{
            check1 = checkEmail()
            check2 = checkName()
            check3 = checkPw()
            check4 = checkPwCheck()
            if(check1 && check2 && check3 && check4) {
                createAccount(email.text.toString(),pw.text.toString(), name.text.toString())
            } else {
                if(!check1) {
                    showAlertDialog("회원가입 오류","이메일을 확인해주세요",false)
                } else if(!check2) {
                    showAlertDialog("회원가입 오류","닉네임을 확인해주세요.",false)
                } else if(!check3) {
                    showAlertDialog("회원가입 오류","비밀번호는 특수기호, 문자, 숫자를 사용해 8자 이상 16자 이하로 해주세요.",false)
                } else if(!check4) {
                    showAlertDialog("회원가입 오류","비밀번호와 확인이 서로 일치하지 않습니다.",false)
                }
            }

        }
    }

    private fun showAlertDialog(title: String, message: String, isPositiveBtnListener: Boolean ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        if(isPositiveBtnListener) {
            builder.setPositiveButton("확인") { _: DialogInterface, _: Int ->
                startActivity(Intent(this, SubActivity::class.java))
            }
        }
        else builder.setPositiveButton("확인", null)
        builder.show()
    }

    private fun createAccount(email: String, password: String, name: String) {
        val db = Firebase.firestore
        auth.createUserWithEmailAndPassword(email, password) //파베에 있는 email password 불러오기
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { //성공적일 경우 수행되는 일들
                    val user = auth.currentUser // user는 파베의 올바른 user가 됨
                    val profileCreateOrUpdates = userProfileChangeRequest { //표시되는 이름(닉네임) 및 프로필 사진의 URL 지정
                        displayName=name
                        photoUri=Uri.parse("https://firebasestorage.googleapis.com/v0/b/today-kotlin.appspot.com/o/profile0.png?alt=media&token=1aee06c2-42d3-4700-b72c-8e2bc71227f1") //그래서 이름이랑 사진 불러옴
                    }
                    user!!.updateProfile(profileCreateOrUpdates) //파이어베이스 Auth에서 제공하는 프로필을 업데이트 하는 과정
                        .addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            val docu = db.collection("words").document("mGHEB2dhXFQkPF8KJww2") // Firestore에서 행운아이템 및 오늘의 한마디가 지정되어 있는 문서 받아옴
                            docu.get().addOnSuccessListener { document ->                                                  // 이건 파이어베이스 안보면 이해 못함
                                val wordList: ArrayList<String> = document.data?.get("words") as ArrayList<String>
                                val itemList: ArrayList<String> = document.data?.get("itemName") as ArrayList<String>
                                val itemSrc: ArrayList<String> = document.data?.get("itemSrc") as ArrayList<String>
                                val itemIdx = Random().nextInt(itemList.size)
                                val todayItem: String = itemList[itemIdx]
                                val todayItemSrc: String = itemSrc[itemIdx]
                                val todayWords: String = wordList[Random().nextInt(wordList.size)]
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

                                db.collection("users").document(user.uid).set(firestoreData).addOnSuccessListener { //Firestore에 사용자의 행운아이템 및 오늘의한마디 문서 생성(업데이트)
                                    showAlertDialog("회원가입 성공","회원이 되신 것을 축하드립니다!", true)
                                }

                            }

                        } else {
                            showAlertDialog("회원가입 오류",task.exception?.message.toString(), false)
                        }
                    }

                } else {
                    showAlertDialog("회원가입 오류",task.exception?.message.toString(), false)
                }
            }
    }
}

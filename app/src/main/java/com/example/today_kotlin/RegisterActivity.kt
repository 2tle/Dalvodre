package com.example.today_kotlin

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.blue
import android.graphics.Color.red
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
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
        val emailValidation =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        var check1 = false
        var check2 = false
        var check3 = false
        var check4 = false //여기까지 선언


        ccBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        } //취소 버튼 눌렀을 때 다시 로그인 화면으로 돌아가기

        pw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?){ //텍스트 입력 후 일어나는 일. 없는데 안쓰면 오류나서 같이 넣음
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { //텍스트 입력 전 일어나는 일, 없는데 안쓰면 오류나서 같이 넣음
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { //텍스트 입력 중 일어나는 일, 올바르게 작성시 check1을 true로 반환한다
                check1 = pw.length() >= 8 && pw != null
            }
        }) //비밀번호 8자 이하거나 공백이면 회원가입 불가

        pwCheck.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                check2 = pw.text.toString() == pwCheck.text.toString() && pwCheck != null //형식에 맞으면 check2를 true로 반환
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }) //비밀번호 재확인에서 위의 비밀번호와 다르거나 공백이면 회원가입 불가


        fun checkEmail() {
            var email = email.text.toString().trim() //공백제거
            val p = Pattern.matches(emailValidation, email) // 입력된 이메일이 주어진 형식에 맞는지 확인
            check3 = p //형식에 맞으면 check3을 true로 반환
        } //이메일 판단 함수

        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkEmail()
            }
        }) //이메일 형식 오류시 회원가입 불가

        name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                check4 = name.length() < 8 && !name.equals(" ") //형식에 맞으면 check4를 true로 반환
            }
        }) //이름이 8자 초과이거나 공백 포함인경우 회원가입 불가

        if (check2 && check1 && check3 && check4) { //모두 true라면 버튼 활성화, 아니라면 비활성화 유지
            regBtn.isClickable = true
            regBtn.isEnabled = true
        } else {
            regBtn.isClickable = false
            regBtn.isEnabled = false
        }
        regBtn.setOnClickListener{
            createAccount(email.text.toString(),pw.text.toString(), name.text.toString()) } //버튼 눌렀을 때 저장됨
    }

    /*val builder = AlertDialog.Builder(this)
        builder.setTitle("회원가입 오류")
        builder.setMessage("형식에 맞게 입력해주세요.")
        builder.setPositiveButton("확인", null) //형식 오류
        regBtn.setOnClickListener {
            if(pw.text.toString() != "" && pw.text.toString() == pwCheck.text.toString() && email.text.toString() != "" && name.text.toString() != "")
                createAccount(email.text.toString(),pw.text.toString(), name.text.toString())
            else {
                builder.show()
                }
            }
        } //회원가입 버튼을 눌렀을 때 형식에 맞는지 확인하고 파베에 데이터 넣기 아니라면 형식오류 출력 예전꺼임
    }*/

    private fun createAccount(email: String, password: String, name: String) {
        val db = Firebase.firestore
        val builder = AlertDialog.Builder(this)
        auth.createUserWithEmailAndPassword(email, password) //파베에 있는 email password 불러오기
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { //성공적일 경우 수행되는 일들
                    val user = auth.currentUser // user는 파베의 올바른 user가 됨
                    val profileCreateOrUpdates = userProfileChangeRequest {
                        displayName=name
                        photoUri=Uri.parse("https://firebasestorage.googleapis.com/v0/b/today-kotlin.appspot.com/o/profile0.png?alt=media&token=1aee06c2-42d3-4700-b72c-8e2bc71227f1"); //그래서 이름이랑 사진 불러옴
                    }
                    user!!.updateProfile(profileCreateOrUpdates) //여기부터 이해안됨,,,누군가 이 주석을 보고 있다면 스피드왜건처럼 설명을 달아주세요
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
                                    builder.show() //성공했을 때 보이는 단어


                                }

                            }

                        } else {
                            builder.setTitle("회원가입 오류")
                            builder.setMessage("회원가입에 실패하였습니다. 관리자에게 문의하세요.")
                            builder.setPositiveButton("확인", null)
                            builder.show() //안에 있는 if문을 만족시키지 못했을 경우 발생
                        }
                    }

                } else {
                    builder.setTitle("회원가입 오류")
                    builder.setMessage("회원가입에 실패하였습니다. 관리자에게 문의하세요.")
                    builder.setPositiveButton("확인", null)
                    builder.show() //밖의 if문을 만족시키지 못했을 경우 발생
                }
            }
    }
}
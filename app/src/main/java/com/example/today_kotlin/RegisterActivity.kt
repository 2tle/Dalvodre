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
        val background : LinearLayout = findViewById(R.id.registerBack) //???????????? ??????

        if(formatted.toInt() in 5..16)
            background.setBackgroundResource(R.drawable.not)
        if(formatted.toInt() in 16..21)
            background.setBackgroundResource(R.drawable.dinner) //????????? ?????? ?????? ??????

        ccBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        } //?????? ?????? ????????? ??? ?????? ????????? ???????????? ????????????


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
                    showAlertDialog("???????????? ??????","???????????? ??????????????????",false)
                } else if(!check2) {
                    showAlertDialog("???????????? ??????","???????????? ??????????????????.",false)
                } else if(!check3) {
                    showAlertDialog("???????????? ??????","??????????????? ????????????, ??????, ????????? ????????? 8??? ?????? 16??? ????????? ????????????.",false)
                } else if(!check4) {
                    showAlertDialog("???????????? ??????","??????????????? ????????? ?????? ???????????? ????????????.",false)
                }
            }

        }
    }

    private fun showAlertDialog(title: String, message: String, isPositiveBtnListener: Boolean ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        if(isPositiveBtnListener) {
            builder.setPositiveButton("??????") { _: DialogInterface, _: Int ->
                startActivity(Intent(this, SubActivity::class.java))
            }
        }
        else builder.setPositiveButton("??????", null)
        builder.show()
    }

    private fun createAccount(email: String, password: String, name: String) {
        val db = Firebase.firestore
        auth.createUserWithEmailAndPassword(email, password) //????????? ?????? email password ????????????
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { //???????????? ?????? ???????????? ??????
                    val user = auth.currentUser // user??? ????????? ????????? user??? ???
                    val profileCreateOrUpdates = userProfileChangeRequest { //???????????? ??????(?????????) ??? ????????? ????????? URL ??????
                        displayName=name
                        photoUri=Uri.parse("https://firebasestorage.googleapis.com/v0/b/today-kotlin.appspot.com/o/profile0.png?alt=media&token=1aee06c2-42d3-4700-b72c-8e2bc71227f1") //????????? ???????????? ?????? ?????????
                    }
                    user!!.updateProfile(profileCreateOrUpdates) //?????????????????? Auth?????? ???????????? ???????????? ???????????? ?????? ??????
                        .addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            val docu = db.collection("words").document("mGHEB2dhXFQkPF8KJww2") // Firestore?????? ??????????????? ??? ????????? ???????????? ???????????? ?????? ?????? ?????????
                            docu.get().addOnSuccessListener { document ->                                                  // ?????? ?????????????????? ????????? ?????? ??????
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

                                db.collection("users").document(user.uid).set(firestoreData).addOnSuccessListener { //Firestore??? ???????????? ??????????????? ??? ?????????????????? ?????? ??????(????????????)
                                    showAlertDialog("???????????? ??????","????????? ?????? ?????? ??????????????????!", true)
                                }

                            }

                        } else {
                            showAlertDialog("???????????? ??????",task.exception?.message.toString(), false)
                        }
                    }

                } else {
                    showAlertDialog("???????????? ??????",task.exception?.message.toString(), false)
                }
            }
    }
}

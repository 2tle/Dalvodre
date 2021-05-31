package com.example.today_kotlin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class settingActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        var user = Firebase.auth.currentUser
        var profileURL: String = "";
        val profileEmail = findViewById<TextView>(R.id.settext4)
        profileEmail.text = user?.email.toString()
        val profileImageButton1 = findViewById<ImageButton>(R.id.profile1)
        val profileImageButton2 = findViewById<ImageButton>(R.id.profile2)
        val profileImageButton3 = findViewById<ImageButton>(R.id.profile3)
        val profileImageButton4 = findViewById<ImageButton>(R.id.profile4)
        val profileImageButton5 = findViewById<ImageButton>(R.id.profile5)
        val profileUsername = findViewById<EditText>(R.id.set_username)
        val profilePw = findViewById<EditText>(R.id.set_pw)
        val profilePwCh = findViewById<EditText>(R.id.set_pwch)
        val regBtn = findViewById<Button>(R.id.regBtn)
        val ccBtn = findViewById<Button>(R.id.ccBtn)
        profileImageButton1.setOnClickListener {
            profileURL = "https://firebasestorage.googleapis.com/v0/b/today-kotlin.appspot.com/o/profile0.png?alt=media&token=1aee06c2-42d3-4700-b72c-8e2bc71227f1";
        }
        profileImageButton2.setOnClickListener {
            profileURL = "https://firebasestorage.googleapis.com/v0/b/today-kotlin.appspot.com/o/profil1.png?alt=media&token=e5458af0-85c6-4245-b481-dc9483a3c8f3";
        }
        profileImageButton3.setOnClickListener {
            profileURL = "https://firebasestorage.googleapis.com/v0/b/today-kotlin.appspot.com/o/profil2.png?alt=media&token=0063079e-b621-45e2-b0e5-b5ec8d9b1586";
        }
        profileImageButton4.setOnClickListener {
            profileURL = "https://firebasestorage.googleapis.com/v0/b/today-kotlin.appspot.com/o/profil3.png?alt=media&token=800243fc-54db-412c-9ee0-82229562b41b";
        }
        profileImageButton5.setOnClickListener {
            profileURL = "https://firebasestorage.googleapis.com/v0/b/today-kotlin.appspot.com/o/profil4.png?alt=media&token=cf27df74-bcb7-47ae-8a50-dacb42ec327e";
        }
        ccBtn.setOnClickListener{
            startActivity(Intent(this, Main2Activity::class.java))
        }
        regBtn.setOnClickListener {
            if(profilePw.text.toString() == profilePwCh.text.toString() && profilePw.text.toString() != "" && profileUsername.text.toString() != "") {
                if(profileURL == "") profileURL = "https://firebasestorage.googleapis.com/v0/b/today-kotlin.appspot.com/o/profile0.png?alt=media&token=1aee06c2-42d3-4700-b72c-8e2bc71227f1";
                val profileUpdates = userProfileChangeRequest {
                    displayName = profileUsername.text.toString()
                    photoUri = Uri.parse(profileURL)
                }
                user!!.updateProfile(profileUpdates).addOnCompleteListener{task ->
                    if(task.isSuccessful) {
                        user!!.updatePassword(profilePw.text.toString()).addOnCompleteListener{task1 ->
                            if(task1.isSuccessful) {
                                val builder = AlertDialog.Builder(this)
                                builder.setTitle("정보 변경 완료")
                                builder.setMessage("회원님의 정보가 변경되었습니다.")
                                builder.setPositiveButton("확인",null)
                                builder.show()
                                startActivity(Intent(this, Main2Activity::class.java))
                            }
                        }
                    }
                }
            }
        }





    }

}
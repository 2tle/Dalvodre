package com.example.today_kotlin

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SettingActivity : AppCompatActivity() {
    @SuppressLint("CutPasteId")
    val fsDb = Firebase.firestore
    lateinit var userPw: String;
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        var profileURL = ""
        val profileEmail = findViewById<TextView>(R.id.settext4)

        val profileImageButton1 = findViewById<ImageButton>(R.id.profile1)
        val profileImageButton2 = findViewById<ImageButton>(R.id.profile2)
        val profileImageButton3 = findViewById<ImageButton>(R.id.profile3)
        val profileImageButton4 = findViewById<ImageButton>(R.id.profile4)
        val profileImageButton5 = findViewById<ImageButton>(R.id.profile5)
        val profileUsername : EditText= findViewById(R.id.set_username)
        val profilePw = findViewById<EditText>(R.id.set_pw)
        val profilePwCh = findViewById<EditText>(R.id.set_pwch)
        val regBtn = findViewById<Button>(R.id.regBtn)
        val ccBtn = findViewById<Button>(R.id.ccBtn)
        val nickName = findViewById<EditText>(R.id.set_username)
        val logoutBtn: Button = findViewById(R.id.logoutBtn)
        val deleteBtn: Button = findViewById(R.id.deleteBtn)
        val auth = FirebaseAuth.getInstance()
        var user = auth.currentUser
        nickName.setText(user?.displayName)
        profileEmail.text = user?.email.toString()
        logoutBtn.setOnClickListener {
            Firebase.auth.signOut()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("???????????? ??????")
            builder.setMessage("???????????? ???????????????. ?????????????????? ???????????????.")
            builder.setPositiveButton("??????") { _: DialogInterface, _: Int ->
                startActivity(Intent(this, LoginActivity::class.java))
            }
            builder.show()
        }

        deleteBtn.setOnClickListener {
            if(profilePw.text.toString() == "") {
                showAlertDialog("???????????? ??????","?????? ??????????????? ??????????????????.",false)
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("????????????")
                builder.setMessage("????????????????????????? ?????? ????????? ????????? ?????? ?????? ???????????????.")
                builder.setNegativeButton("??????",null)
                builder.setPositiveButton("??????") { _:DialogInterface, _:Int ->
                    val credential = EmailAuthProvider.getCredential(user.email, profilePw.text.toString())
                    user.reauthenticate(credential).addOnCompleteListener{
                        userPw = profilePw.text.toString()
                        delFirestoreUsers(Firebase.auth.currentUser?.uid)
                    }.addOnFailureListener {
                        val builder2 = AlertDialog.Builder(this)
                        builder2.setTitle("???????????? ??????")
                        builder2.setMessage("?????? ???????????? ??????")
                        builder2.setPositiveButton("??????",null)
                        builder2.show()
                    }
                }
                builder.show()
            }
        }

        profileImageButton1.setOnClickListener {
            profileURL =
                "https://firebasestorage.googleapis.com/v0/b/today-kotlin.appspot.com/o/profile0.png?alt=media&token=1aee06c2-42d3-4700-b72c-8e2bc71227f1"
        }
        profileImageButton2.setOnClickListener {
            profileURL =
                "https://firebasestorage.googleapis.com/v0/b/today-kotlin.appspot.com/o/profil1.png?alt=media&token=e5458af0-85c6-4245-b481-dc9483a3c8f3"
        }
        profileImageButton3.setOnClickListener {
            profileURL =
                "https://firebasestorage.googleapis.com/v0/b/today-kotlin.appspot.com/o/profil2.png?alt=media&token=0063079e-b621-45e2-b0e5-b5ec8d9b1586"
        }
        profileImageButton4.setOnClickListener {
            profileURL =
                "https://firebasestorage.googleapis.com/v0/b/today-kotlin.appspot.com/o/profil3.png?alt=media&token=800243fc-54db-412c-9ee0-82229562b41b"
        }
        profileImageButton5.setOnClickListener {
            profileURL =
                "https://firebasestorage.googleapis.com/v0/b/today-kotlin.appspot.com/o/profil4.png?alt=media&token=cf27df74-bcb7-47ae-8a50-dacb42ec327e"
        }
        ccBtn.setOnClickListener {
            startActivity(Intent(this, Main2Activity::class.java))
        }
        regBtn.setOnClickListener {
            if (profilePwCh.text.toString() != "" && profilePw.text.toString() != "" && profileUsername.text.toString() != "" && profilePwCh.text.toString().length >= 8) {
                if (profileURL == "") profileURL =
                    "https://firebasestorage.googleapis.com/v0/b/today-kotlin.appspot.com/o/profile0.png?alt=media&token=1aee06c2-42d3-4700-b72c-8e2bc71227f1"
                val profileUpdates = userProfileChangeRequest {
                    displayName = profileUsername.text.toString()
                    photoUri = Uri.parse(profileURL)
                }
                user!!.reauthenticate(
                    EmailAuthProvider.getCredential(
                        profileEmail.text as String,
                        profilePw.text.toString()
                    )
                ).addOnSuccessListener {
                    user.updateProfile(profileUpdates).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            user.updatePassword(profilePwCh.text.toString())
                                .addOnCompleteListener { task1 ->
                                    if (task1.isSuccessful) {
                                        showAlertDialog("?????? ?????? ??????", "????????? ?????????????????????.", true)
                                    } else {
                                        showAlertDialog(
                                            "?????? ?????? ??????",
                                            task1.exception?.message.toString(),
                                            false
                                        )
                                    }
                                }
                        } else {
                            showAlertDialog("?????? ?????? ??????", task.exception?.message.toString(), false)
                        }
                    }
                }.addOnFailureListener {
                    showAlertDialog("?????? ?????? ??????", "????????? ??????. ??????????????? ??????????????????.", false)
                }

            } else {
                showAlertDialog("?????? ?????? ??????", "????????? ??????????????????.", false)
            }
        }


    }

    private fun showAlertDialog(title: String, message: String, isPositiveBtnListener: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        if (isPositiveBtnListener) {
            builder.setPositiveButton("??????") { _: DialogInterface, _: Int ->
                startActivity(Intent(this, Main2Activity::class.java))
            }
        } else {
            builder.setPositiveButton("??????", null)
        }

        builder.show()
    }

    fun delFirestoreUsers(uid : String?) {
        fsDb.collection("users").document(uid.toString()).delete().addOnCompleteListener { task ->
            if(task.isSuccessful) {
                delFirestorePosts(uid)
            }
        }
    }
    fun delFirestorePosts(uid: String?) {
        fsDb.collection("posts").whereEqualTo("uid",uid).get().addOnSuccessListener { docs ->
            if(docs.size() == 0) {
                delAuth()
            } else {
                var cnt : Int = 0
                for(doc in docs) {
                    fsDb.collection("posts").document(doc.data["uid"].toString()).delete().addOnSuccessListener {
                        cnt += 1
                        if(cnt == docs.size()) {
                            delAuth()
                        }
                    }.addOnFailureListener {
                        delAuth()
                    }
                }
            }


        }.addOnFailureListener {
            delAuth()
        }
    }

    fun delAuth() {
        val user = Firebase.auth.currentUser
        val credential = EmailAuthProvider.getCredential(user.email.toString(), userPw)
        user.reauthenticate(credential).addOnCompleteListener {
            user.delete().addOnCompleteListener  { task ->
                Firebase.auth.signOut()
                val builder1 = AlertDialog.Builder(this)
                if(task.isSuccessful) {

                    builder1.setTitle("???????????? ??????")
                    builder1.setMessage("???????????? ???????????????. ?????????????????? ???????????????.")
                    builder1.setPositiveButton("??????") { _: DialogInterface, _: Int ->
                        startActivity(Intent(baseContext, LoginActivity::class.java))
                    }
                } else {
                    builder1.setTitle("???????????? ??????")
                    builder1.setMessage("??????????????? ?????????????????????. ??????????????? ???????????????.")
                    builder1.setPositiveButton("??????",null)
                }
                builder1.show()

            }
        }

    }

}
package com.example.today_kotlin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.today_kotlin.ui.community.CommunityViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val backBtn : ImageButton = findViewById(R.id.ic_back)
        backBtn.setOnClickListener {
            startActivity(Intent(this,Main2Activity::class.java))
        }

        val tWord : TextView = findViewById(R.id.write_word)
        tWord.text = intent.extras?.getString("words").toString()
        val tUserInput : EditText = findViewById(R.id.write_post)
        tUserInput.setText(intent.extras?.getString("text").toString())
        when(intent.extras?.getInt("background")) {
            1 -> {
                tWord.setBackgroundResource(R.drawable.not)
            } 2 -> {
                tWord.setBackgroundResource(R.drawable.dinner)
            } 3 -> {
                tWord.setBackgroundResource(R.drawable.main)
            }
        }

        val sendBtn : ImageButton = findViewById(R.id.ic_send)
        sendBtn.setOnClickListener {
            updatePost(tUserInput.text.toString(), intent.extras?.getString("documentID").toString())
        }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun updatePost(message: String, docuId: String) {
        val db = Firebase.firestore
        val dt = hashMapOf(
            "text" to message
        )
        db.collection("posts").document(docuId).set(dt, SetOptions.merge()).addOnSuccessListener {
            showAlertDialog("수정 완료!","수정이 완료되었습니다.");
        }.addOnFailureListener { e->
            showAlertDialog("오류발생",e.message.toString());
        }

    }

    private fun showAlertDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("확인"){_: DialogInterface, _:Int ->
            startActivity(Intent(baseContext,Main2Activity::class.java))
        }
        builder.show()
    }
}
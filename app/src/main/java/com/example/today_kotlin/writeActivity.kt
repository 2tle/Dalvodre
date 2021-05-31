package com.example.today_kotlin

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNCHECKED_CAST"
)
class writeActivity : AppCompatActivity() {

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        val dateAndtime: LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH")
        val formatted = dateAndtime.format(formatter)
        val background: TextView = findViewById(R.id.write_word)
        val sendBtn = findViewById<ImageButton>(R.id.ic_send)
        val words: TextView = findViewById(R.id.write_word)
        val text = findViewById<EditText>(R.id.write_post)
        var backgroundType: Int = 0
        val user = Firebase.auth.currentUser

        if(formatted.toInt() in 5..18) {
            background.setBackgroundResource(R.drawable.not)
            backgroundType = 1 // not
        }else if(formatted.toInt() in 16..21) {
            background.setBackgroundResource(R.drawable.dinner)
            backgroundType = 2
        }

        sendBtn.setOnClickListener{
            sendPost(user.uid,user.photoUrl.toString(),user.displayName, words.text.toString(), text.text.toString(), backgroundType );
        }




        val db = Firebase.firestore
        val docRef = db.collection("users").document(user.uid)
        docRef.get().addOnSuccessListener { document ->
            if(document != null) {
                if(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) == document.data?.get("date")) {
                    val todayWords = document.data?.get("todayWords") as String
                    words.text = todayWords
                } else {
                    val newListGiveMe = db.collection("words").document("mGHEB2dhXFQkPF8KJww2")
                    newListGiveMe.get().addOnSuccessListener { document1 ->
                        val wordList: ArrayList<String> = document1.data?.get("words") as ArrayList<String>
                        val todayWords: String = wordList.get(Random().nextInt(wordList.size))
                        val listWords: ArrayList<String> = document.data?.get("listWords") as ArrayList<String>
                        listWords.add(todayWords)
                        val firestoreData = hashMapOf(
                            "date" to LocalDateTime.now().format(DateTimeFormatter.ISO_DATE),
                            "todayWords" to todayWords,
                            "listWords" to listWords
                        )
                        docRef.set(firestoreData).addOnSuccessListener {
                            words.text= todayWords
                        }
                    }
                }
            }
        }


    }

    private fun sendPost(uid: String, profileId: String, username: String, words: String,text: String, backgroundType: Int) {
        val db = Firebase.firestore
        val data = hashMapOf(
            "uid" to uid,
            "name" to username,
            "profileId" to profileId,
            "words" to words,
            "text" to text,
            "heart" to arrayListOf<String>(),
            "backgroundType" to backgroundType,
            "date" to LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)
        )
        db.collection("posts").document().set(data).addOnSuccessListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("업로드 성공")
            builder.setMessage("너나들이에 성공적으로 포스트가 업로드 되었습니다.")
            builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                startActivity(Intent(this, Main2Activity::class.java))
            }
            builder.show()

        }

    }
}
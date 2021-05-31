package com.example.today_kotlin.ui.home

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.today_kotlin.R
import com.example.today_kotlin.subActivity
import com.example.today_kotlin.writeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    lateinit var storage: FirebaseStorage
    private lateinit var homeViewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        val user = Firebase.auth.currentUser
        val db = Firebase.firestore
        val communityBtn: Button = root.findViewById(R.id.communityBtn)
        communityBtn.setOnClickListener {
            startActivity(Intent(activity, writeActivity::class.java))
        }
        val docRef = db.collection("users").document(user.uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    if(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) == document.data?.get("date")) {
                        homeViewModel.text.observe(viewLifecycleOwner, Observer {
                            textView.text = document.data?.get("todayWords") as String
                        })

                    } else {
                        val newListGiveMe = db.collection("words").document("mGHEB2dhXFQkPF8KJww2")
                        newListGiveMe.get().addOnSuccessListener { document1 ->
                            val wordList: ArrayList<String> = document1.data?.get("words") as ArrayList<String>
                            val todayWords: String = wordList.get(Random().nextInt(wordList.size))
                            val listWords: ArrayList<String> = document.data?.get("listWords") as ArrayList<String>
                            val listDates: ArrayList<String> = document.data?.get("listDates") as ArrayList<String>
                            listWords.add(todayWords)
                            listDates.add(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE).toString())
                            val firestoreData = hashMapOf(
                                "date" to LocalDateTime.now().format(DateTimeFormatter.ISO_DATE),
                                "todayWords" to todayWords,
                                "listWords" to listWords,
                                "listDates" to listDates
                            )
                            //Log.d("<<>>",firestoreData.toString());
                            db.collection("users").document(user.uid).set(firestoreData).addOnSuccessListener {
                                homeViewModel.text.observe(viewLifecycleOwner, Observer {
                                    textView.text = todayWords
                                })


                            }
                        }
                    }
                }
            }
        return root

    }
}
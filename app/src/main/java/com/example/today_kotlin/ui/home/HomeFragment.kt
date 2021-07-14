package com.example.today_kotlin.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.today_kotlin.R
import com.example.today_kotlin.WriteActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNCHECKED_CAST")
class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView = root.findViewById<TextView>(R.id.text_home)
        val itemImgView = root.findViewById<ImageView>(R.id.item_imageView)
        val itemText = root.findViewById<TextView>(R.id.item_nametext)
        val user = Firebase.auth.currentUser
        val db = Firebase.firestore
        val communityBtn = root.findViewById<Button>(R.id.communityBtn)
        val handler = Handler(Looper.myLooper()!!)
        val dateTime: LocalDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH")
        val formatted = dateTime.format(formatter)
        val background = root.findViewById<TextView>(R.id.text_home) //여기까지 선언


        if(formatted.toInt() in 5..15)
            background.setBackgroundResource(R.drawable.not)
        if(formatted.toInt() in 16..21)
            background.setBackgroundResource(R.drawable.dinner) //시간에 따라 테마 변경

        communityBtn.setOnClickListener {
            startActivity(Intent(activity, WriteActivity::class.java))
        }
        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        val docRef = db.collection("users").document(user.uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    if(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) == document.data?.get("date")) {
                        homeViewModel.text.observe(viewLifecycleOwner,  {
                            val httpsReference = storage.getReferenceFromUrl(document.data?.get("itemSrc") as String)
                            Glide.with(root).load(httpsReference).into(itemImgView)
                            textView.text = document.data?.get("todayWords") as String
                            itemText.text = document.data?.get("item") as String

                        })

                    } else {
                        val newListGiveMe = db.collection("words").document("mGHEB2dhXFQkPF8KJww2")
                        newListGiveMe.get().addOnSuccessListener { document1 ->
                            val wordList: ArrayList<String> = document1.data?.get("words") as ArrayList<String>
                            val itemList: ArrayList<String> = document1.data?.get("itemName") as ArrayList<String>
                            val itemSrc: ArrayList<String> = document1.data?.get("itemSrc") as ArrayList<String>
                            val itemIdx = Random().nextInt(itemList.size)
                            val todayItem: String = itemList[itemIdx]
                            val todayItemSrc: String = itemSrc[itemIdx]
                            val todayWords: String = wordList[Random().nextInt(wordList.size)]
                            val listWords: ArrayList<String> = document.data?.get("listWords") as ArrayList<String>
                            val listDates: ArrayList<String> = document.data?.get("listDates") as ArrayList<String>
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
                                homeViewModel.text.observe(viewLifecycleOwner, {
                                    val httpsReference = storage.getReferenceFromUrl(todayItemSrc)
                                    Glide.with(root).load(httpsReference).into(itemImgView)
                                    textView.text = todayWords
                                    itemText.text = todayItem
                                })


                            }
                        }
                    }
                }
            }
        return root

    }
}
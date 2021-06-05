package com.example.today_kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.today_kotlin.R.drawable
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class CommunityAdapter(private val context: Context): RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {
    var dates = mutableListOf<CommunityData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_community, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dates.size

    override fun onBindViewHolder(holder: CommunityAdapter.ViewHolder, position: Int) {
        holder.bind(dates[position])
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val user = Firebase.auth.currentUser
        private val db = Firebase.firestore
        private val profileImgView : ImageView = itemView.findViewById(R.id.community_imageview)
        private val username: TextView = itemView.findViewById(R.id.community_username)
        private val words: TextView = itemView.findViewById(R.id.community_text)
        private val heartBtn: ImageButton = itemView.findViewById(R.id.community_imagebutton)
        private val heartCount: TextView = itemView.findViewById(R.id.community_like)
        private val date: TextView = itemView.findViewById(R.id.community_date)
        private val text: TextView = itemView.findViewById(R.id.community_usertext)
        private val documentId: TextView = itemView.findViewById(R.id.documentID)
        fun bind(item: CommunityData) {
            var storage: FirebaseStorage = FirebaseStorage.getInstance()
            val httpsReference = storage.getReferenceFromUrl(item.profileId)
            Glide.with(itemView).load(httpsReference).into(profileImgView)
            username.text = item.name
            words.text = item.words
            heartCount.text = item.heart.size.toString()
            date.text = item.date
            text.text = item.text
            documentId.text = 0.toString()
            if(item.backgroundType == 1) {
                words.setBackgroundResource(drawable.not)
            } else if (item.backgroundType == 2) {
                words.setBackgroundResource(drawable.dinner)
            } else {
                words.setBackgroundResource(drawable.main)
            }
            if(item.heart.contains(user.uid)) {
                heartBtn.setImageResource(drawable.ic_fullheart)
                documentId.text = 1.toString()
            }
            heartBtn.setOnClickListener {
                if(documentId.text == "1") {
                    val docRef1 = db.collection("posts").document(item.docuUid)
                    docRef1.get().addOnSuccessListener { document1 ->
                        if(document1 != null) {
                            val heartList: ArrayList<String> = document1.data?.get("heart") as ArrayList<String>
                            heartList.remove(user.uid)
                            val data = hashMapOf(
                                "heart" to heartList,
                                "date" to item.date,
                                "backgroundType" to item.backgroundType,
                                "name" to item.name,
                                "profileId" to item.profileId,
                                "text" to item.text,
                                "uid" to item.userUid,
                                "words" to item.words
                            )
                            db.collection("posts").document(item.docuUid).set(data).addOnSuccessListener {
                                heartBtn.setImageResource(drawable.ic_heart)
                                heartCount.text = (Integer.parseInt(heartCount.text as String) - 1).toString()
                                documentId.text = 0.toString()
                            }

                        }
                    }
                } else {
                    val docRef1 = db.collection("posts").document(item.docuUid)
                    docRef1.get().addOnSuccessListener { document1 ->
                        if(document1 != null) {
                            val heartList: ArrayList<String> = document1.data?.get("heart") as ArrayList<String>
                            heartList.add(user.uid)
                            val data = hashMapOf(
                                "heart" to heartList,
                                "date" to item.date,
                                "backgroundType" to item.backgroundType,
                                "name" to item.name,
                                "profileId" to item.profileId,
                                "text" to item.text,
                                "uid" to item.userUid,
                                "words" to item.words
                            )
                            db.collection("posts").document(item.docuUid).set(data).addOnSuccessListener {
                                heartBtn.setImageResource(drawable.ic_fullheart)
                                heartCount.text = (Integer.parseInt(heartCount.text as String) +  1).toString()
                                documentId.text = 1.toString()
                            }

                        }
                    }
                }


            }
            // item.heart 리스트에 본인id 있으면, 그때는 색칠해서 넣기
            // 하트버튼 구현하기

        } //근데 이건 정상적으로 될껄 안되면 이전보다 심각한건데
    }

}
package com.example.today_kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import org.w3c.dom.Text

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
            documentId.text = item.docuUid
            if(item.backgroundType == 1) {
                words.setBackgroundResource(R.drawable.not)
            } else if (item.backgroundType == 2) {
                words.setBackgroundResource(R.drawable.dinner)
            } else {
                words.setBackgroundResource(R.drawable.main)
            }

            // item.heart 리스트에 본인id 있으면, 그때는 색칠해서 넣기
            // 하트버튼 구현하기

        }
    }

}
package com.example.today_kotlin.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.today_kotlin.CommunityAdapter
import com.example.today_kotlin.CommunityData
import com.example.today_kotlin.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Suppress("UNCHECKED_CAST")
class CommunityFragment : Fragment() {

    private lateinit var communityViewModel1: CommunityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        communityViewModel1 = ViewModelProvider(this).get(CommunityViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_community, container, false)
        //val textView: TextView = root.findViewById(R.id.text_community)
        val communityAdapter1 = CommunityAdapter(requireContext())
        val recycler = root.findViewById<RecyclerView>(R.id.community_recycler)
        recycler.adapter = communityAdapter1
        val list1 = mutableListOf<CommunityData>()
        val db = Firebase.firestore
        val docRef = db.collection("posts")
        docRef.get().addOnSuccessListener { result ->
            list1.apply {
                for(document in result) {
                    val doc = document.data
                    add(CommunityData(Integer.parseInt(doc?.get("backgroundType").toString()), doc?.get("date") as String, doc?.get("name") as String,
                        doc?.get("profileId") as String, doc?.get("text") as String, doc?.get("words") as String, doc?.get("uid") as String, doc?.get("heart") as ArrayList<String>, document.id))
                }
                communityAdapter1.dates = list1
                communityAdapter1.notifyDataSetChanged()
            }

        }

        return root
    }
}
package com.example.today_kotlin.ui.save

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.today_kotlin.ListWordsAdapter
import com.example.today_kotlin.ListWordsData
import com.example.today_kotlin.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SaveFragment : Fragment() {

    private lateinit var saveViewModel: SaveViewModel
    lateinit var listWordsAdapter: ListWordsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        saveViewModel =
            ViewModelProvider(this).get(SaveViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_save, container, false)
        val listWordsAdapter = ListWordsAdapter(requireContext())
        val recycler = root.findViewById<RecyclerView>(R.id.recycler)
        recycler.adapter = listWordsAdapter
        //val list1 = ArrayList<ListWordsData>()
        val list1 = mutableListOf<ListWordsData>()
        val user = Firebase.auth.currentUser
        val db = Firebase.firestore
        val docRef = db.collection("users").document(user?.uid.toString())
        docRef.get().addOnSuccessListener { document ->
            if(document != null) {
                val listWords = document.data?.get("listWords") as ArrayList<*>
                val listDates = document.data?.get("listDates") as ArrayList<*>
                val listCount = listWords.size - 1

                list1.apply {
                    for(i in 0..listCount) {
                        add(ListWordsData(words = listWords[i] as String, date = listDates[i] as String))
                    }
                    listWordsAdapter.dates = list1
                    listWordsAdapter.notifyDataSetChanged()

                }
            }
        }


        return root
    }

}



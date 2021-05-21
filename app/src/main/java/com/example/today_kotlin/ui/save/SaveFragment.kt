package com.example.today_kotlin.ui.save

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.today_kotlin.R

class SaveFragment : Fragment() {

    private lateinit var saveViewModel: SaveViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        saveViewModel =
            ViewModelProvider(this).get(SaveViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_save, container, false)
        val textView: TextView = root.findViewById(R.id.text_save)
        saveViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
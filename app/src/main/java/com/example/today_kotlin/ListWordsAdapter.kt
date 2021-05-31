package com.example.today_kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.today_kotlin.ui.save.SaveFragment

class ListWordsAdapter(private val context: Context) : RecyclerView.Adapter<ListWordsAdapter.ViewHolder>() {
    var dates = mutableListOf<ListWordsData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListWordsAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.imagetext_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dates.size

    override fun onBindViewHolder(holder: ListWordsAdapter.ViewHolder, position: Int) {
        holder.bind(dates[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val words : TextView = itemView.findViewById(R.id.text_ex)
        private val date : TextView = itemView.findViewById(R.id.date_ex)
        fun bind(item: ListWordsData) {
            words.text = item.words
            date.text = item.date
        }
    }
}
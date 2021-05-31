package com.example.today_kotlin
/*그동안의 한마디 어뎁터, 완성X*/
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/*class Recycler_Adapter(val context: Context, val Meantime, ArrayList<Recycler_Data>):
    RecyclerView.Adapter<>() {

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val photo = itemView?.findViewById<ImageView>(R.id.image_ex)
        val word = itemView?.findViewById<TextView>(R.id.text_ex)

        fun bind (meantime: Recycler_Data, context: Context) {
            /* Photo의 setImageResource에 들어갈 이미지의 id를 파일명(String)으로 찾고,
            이미지가 없는 경우 안드로이드 기본 아이콘을 표시한다.*/
            if (meantime.Photo != "") {
                val resourceId = context.resources.getIdentifier(meantime.Photo, "drawable", context.packageName)
                photo?.setImageResource(resourceId)
            } else {
                photo?.setImageResource(R.mipmap.ic_launcher)
            }
            /* 나머지 TextView(word : 그동안의 한마디)와 String 데이터를 연결해야함 ,아래에 코드 써야하는데 못했어..*/
        }
    }
}*/
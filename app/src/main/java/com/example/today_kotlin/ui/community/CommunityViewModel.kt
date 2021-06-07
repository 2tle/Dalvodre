package com.example.today_kotlin.ui.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CommunityViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "여기는 커뮤니티가 들어갈거다"
    }
    val text: LiveData<String> = _text
}
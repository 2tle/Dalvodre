package com.example.today_kotlin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//변수 만들고 변수(오늘의 한마디) 값을 아래 value에 넣으면 될듯
class HomeViewModel : ViewModel() {
    var word = "오늘의 한마디"
    private val _text = MutableLiveData<String>().apply {
        value = "\"\n"+word+"\n\""
    }
    val text: LiveData<String> = _text
}
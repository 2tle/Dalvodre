package com.example.today_kotlin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//함수 만들고 함수값을 아래 value에 넣으면 될듯
class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "여기는 오늘의 한마디 들어갈거"
    }
    val text: LiveData<String> = _text
}
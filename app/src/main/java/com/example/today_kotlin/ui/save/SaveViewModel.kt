package com.example.today_kotlin.ui.save

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SaveViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "여기는 그동안 한마디"
    }
    val text: LiveData<String> = _text
}
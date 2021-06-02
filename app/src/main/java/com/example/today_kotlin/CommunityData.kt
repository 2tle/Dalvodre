package com.example.today_kotlin

data class CommunityData (
    val backgroundType: Int,
    val date: String,
    val name: String,
    val profileId: String,
    val text: String,
    val words: String,
    val userUid: String,
    val heart: ArrayList<String>,
    val docuUid: String
    )
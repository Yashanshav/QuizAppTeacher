package com.example.teacherquizupapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quiz(
    val quizName: String = "",
    val noOfQuestions: Int = 0,
    val timeLimit: Int = 0,
    var Questions: ArrayList<Questions> = ArrayList(),
    val date: String = "",
    val time: String = "") : Parcelable
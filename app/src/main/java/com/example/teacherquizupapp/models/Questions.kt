package com.example.teacherquizupapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Questions (
    val ques: String = "",
    val options: ArrayList<String> = ArrayList(),
    val correctAns: String = ""
        ) : Parcelable
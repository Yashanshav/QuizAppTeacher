package com.example.teacherquizupapp.daos

import com.example.teacherquizupapp.models.Quiz
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuizDao {

    private val db = FirebaseFirestore.getInstance()
    val quizCollection = db.collection("quiz")

    fun addQuiz(quiz: Quiz?) {
        quiz?.let {
            GlobalScope.launch {
                quizCollection.document().set(it)
            }
        }
    }

}
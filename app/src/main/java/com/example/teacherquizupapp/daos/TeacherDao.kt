package com.example.teacherquizupapp.daos

import com.example.teacherquizupapp.models.Teachers
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeacherDao {

    private val db = FirebaseFirestore.getInstance()
    private val teacherCollection = db.collection("teachers")

    fun addTeacher(teacher: Teachers?) {
        teacher?.let {
            GlobalScope.launch {
                teacherCollection.document(teacher.tid).set(it)
            }
        }
    }

    fun getTeacherById(tId: String): Task<DocumentSnapshot> {
        return teacherCollection.document(tId).get()
    }
}
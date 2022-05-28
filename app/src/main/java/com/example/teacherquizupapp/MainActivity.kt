package com.example.teacherquizupapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teacherquizupapp.daos.QuizDao
import com.example.teacherquizupapp.models.Quiz
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class MainActivity : AppCompatActivity() {

    private lateinit var quizDao : QuizDao
    private lateinit var adapter : QuizAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val cb = findViewById<Button>(R.id.createButton)

        cb.setOnClickListener {
            startActivity(Intent(this, CreateQuiz:: class.java))
        }

        setUpRecyclerView()


    }

    private fun setUpRecyclerView() {
        quizDao = QuizDao()
        val quizCollection = quizDao.quizCollection
        val query = quizCollection.orderBy("quizName")
        val recyclerOptions = FirestoreRecyclerOptions.Builder<Quiz>().setQuery(query, Quiz::class.java).build()

        // Now the recycler options are pass in adapter to display in firestore recycler view
        adapter = QuizAdapter(recyclerOptions)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false)

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}

class LinearLayoutManagerWrapper(context: Context?, orientation: Int, reverseLayout: Boolean) :
    LinearLayoutManager(
        context,
        orientation,
        reverseLayout
    ) {

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}


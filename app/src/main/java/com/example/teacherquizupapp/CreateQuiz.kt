package com.example.teacherquizupapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teacherquizupapp.models.Questions
import com.example.teacherquizupapp.models.Quiz
import java.util.*


class CreateQuiz : AppCompatActivity() {

    private lateinit var dp: TextView
    private lateinit var tp: TextView
    private lateinit var nq: EditText
    private lateinit var tl: EditText
    private lateinit var addB: Button
    private lateinit var nameQuiz: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_quiz)

        tp = findViewById(R.id.timePicker)
        dp = findViewById(R.id.datePicker)
        nq = findViewById(R.id.noOfQuestions)
        tl = findViewById(R.id.timeLimit)
        addB = findViewById(R.id.addQuestionsButton)
        nameQuiz = findViewById(R.id.quizName)

        tp.setOnClickListener { handleTime() }
        dp.setOnClickListener { handleDate() }

        addB.setOnClickListener {
            if(check()) {

                val qz = Quiz( nameQuiz.text.toString() , nq.text.toString().toInt(), tl.text.toString().toInt(), ArrayList<Questions>(nq.text.toString().toInt()),
                                dp.text.toString(), tp.text.toString())

                goToAddQuestionActivity(qz)

                Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToAddQuestionActivity(qz: Quiz) {
        val intent = Intent(this, AddQuestions :: class.java)
        intent.putExtra("quiz", qz)
        startActivity(intent)
    }

    private fun check(): Boolean {
        if(tp.text.toString().isEmpty() || dp.text.toString().isEmpty() || tl.text.toString().isEmpty() || nq.text.toString().isEmpty()) {
            return false
        }
        return true
    }

    // date picker dialog
    private fun handleDate() {
        val calendar = Calendar.getInstance()
        val ye = calendar[Calendar.YEAR]
        val mo = calendar[Calendar.MONTH]
        val dt = calendar[Calendar.DATE]

        val datePickerDialog = DatePickerDialog(this,
            { _, year, month, date ->
                val calendar1 = Calendar.getInstance()
                calendar1[Calendar.YEAR] = year
                calendar1[Calendar.MONTH] = month
                calendar1[Calendar.DATE] = date
                val dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString()
                dp.text = dateText
            }, ye, mo, dt
        )

        datePickerDialog.show()
    }

    // time picker dialog
    private fun handleTime() {
        val calendar = Calendar.getInstance()
        val hr = calendar[Calendar.HOUR]
        val min = calendar[Calendar.MINUTE]
        val is24HourFormat = DateFormat.is24HourFormat(this)

        val timePickerDialog = TimePickerDialog(this,
            { _, hour, minute ->
                Log.i(TAG, "onTimeSet: $hour$minute")
                val calendar2 = Calendar.getInstance()
                calendar2[Calendar.HOUR] = hour
                calendar2[Calendar.MINUTE] = minute
                val dateText = DateFormat.format("h:mm a", calendar2).toString()
                tp.text = dateText
            }, hr, min, is24HourFormat
        )

        timePickerDialog.show()
    }

}



package com.example.teacherquizupapp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.teacherquizupapp.daos.QuizDao
import com.example.teacherquizupapp.models.Questions
import com.example.teacherquizupapp.models.Quiz

class AddQuestions : AppCompatActivity() {

    private lateinit var scroll: ScrollView
    private lateinit var create: TextView
    private lateinit var question: EditText
    private lateinit var option1: EditText
    private lateinit var option2: EditText
    private lateinit var option3: EditText
    private lateinit var option4: EditText
    private lateinit var correctOption: EditText
    private lateinit var nextButton: Button
    private lateinit var backButton: Button
    private lateinit var qz: Quiz
    private var value: Int = 0
    private var currQ: Int = 1
    private lateinit var questionList: ArrayList<Questions>

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_questions)

        getQuiz()
        initializeViews()

        nextButton.setOnClickListener {
            if(checkVal()) {
                addQuestion()
                updateUI()

                if(currQ == value) nextButton.text = getString(R.string.submit)

                if(currQ > value) {
                    addQuiz()
                    goToNextActivity()
                }
            }
            else Toast.makeText(this, getString(R.string.fill_values), Toast.LENGTH_SHORT).show()
        }

        backButton.setOnClickListener { fillUI() }


    }

    // add Quiz to the firebase DB
    private fun addQuiz() {
        qz.Questions = questionList
        QuizDao().addQuiz(qz)
    }

    // add question in the quiz
    private fun addQuestion() {
        val ques = Questions(question.text.toString(), getL(), correctOption.text.toString())
        questionList.add(ques)
    }

    // get the quiz from previous activity
    private fun getQuiz() {
        qz = intent.getParcelableExtra("quiz")!!
        value = qz.noOfQuestions
        questionList = qz.Questions
    }

    // views are initialized with findViewById method.
    private fun initializeViews() {
        scroll = findViewById(R.id.svaq)
        create = findViewById(R.id.questionCreateNumber)
        question = findViewById(R.id.question)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        correctOption = findViewById(R.id.correctOption)
        nextButton = findViewById(R.id.nextButton)
        backButton = findViewById(R.id.backButton)
        create.text = getString(R.string.questionNumber, currQ, value)
    }

    // Moves to the Main Activity if all the questions are filled.
    private fun goToNextActivity() {
        Toast.makeText(this, getString(R.string.quiz_added_successfully), Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity :: class.java))
        finish()
    }

    // get all the options and store in ArrayList
    private fun getL(): java.util.ArrayList<String> {
        val al = ArrayList<String>()
        al.add(option1.text.toString())
        al.add(option2.text.toString())
        al.add(option3.text.toString())
        al.add(option4.text.toString())

        return al
    }

    // fill UI with previous options
    private fun fillUI() {
        val qs = questionList.removeLast()
        scroll.fullScroll(View.FOCUS_UP)
        create.text = getString(R.string.questionNumber, currQ, value)
        question.setText(qs.ques)
        option1.setText(qs.options[0])
        option2.setText(qs.options[1])
        option3.setText(qs.options[2])
        option4.setText(qs.options[3])
        correctOption.setText(qs.correctAns)
        if(--currQ == 1) {
            backButton.visibility = View.GONE
        }
    }

    // update UI with clear screen
    @RequiresApi(Build.VERSION_CODES.M)
    private fun updateUI() {

        currQ++
        scroll.fullScroll(View.FOCUS_UP)
        backButton.visibility = View.VISIBLE
        question.text.clear()
        option1.text.clear()
        option2.text.clear()
        option3.text.clear()
        option4.text.clear()
        correctOption.text.clear()
        correctOption.clearFocus()
        create.text = getString(R.string.questionNumber, currQ, value)
        hideKeyboard()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun hideKeyboard() {
        try {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            // TODO: handle exception
        }
    }

    // Check if any view is empty or not
    private fun checkVal(): Boolean {
        if(question.text.toString().isEmpty() || option1.text.toString().isEmpty() ||
            option2.text.toString().isEmpty() || option3.text.toString().isEmpty() ||
            option4.text.toString().isEmpty() || correctOption.text.toString().isEmpty())
                return false
        return true
    }
}
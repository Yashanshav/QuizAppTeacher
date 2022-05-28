package com.example.teacherquizupapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.teacherquizupapp.daos.TeacherDao
import com.example.teacherquizupapp.models.Teachers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val tag = "Registration Tag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val username = findViewById<EditText>(R.id.registerEmail)
        val password = findViewById<EditText>(R.id.registerPassword)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val pbr = findViewById<ProgressBar>(R.id.progressBarRegister)

        auth = FirebaseAuth.getInstance()


        registerButton.setOnClickListener {

            registerButton.visibility = View.GONE
            pbr.visibility = View.VISIBLE
            val name = findViewById<EditText>(R.id.registerName)
            val branch = findViewById<EditText>(R.id.registerBranch)
            val regNo = findViewById<EditText>(R.id.registerRegNo)
            createAccount(username.text.toString().trim(), password.text.toString().trim(), name.text.toString().trim(),
                branch.text.toString().trim(), regNo.text.toString().trim())
        }
    }

    private fun createAccount(username: String, password: String, name: String?, branch: String?, regNo: String?) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(tag, "createUserWithEmail:success")
                    val user = task.result.user
                    updateUI(user)
                    Toast.makeText(baseContext, "Registration Successful",
                        Toast.LENGTH_SHORT).show()

                    // database operations should be performed on separate threads to enhance user experience.
                    GlobalScope.launch(Dispatchers.IO) {
                        val teacherDao = TeacherDao()
                        val teacherInfo = Teachers(user?.uid.toString(), name, branch, regNo)
                        teacherDao.addTeacher(teacherInfo)
                    }

                    startActivity(Intent(this, LoginActivity :: class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(tag, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    // update UI according to obtained result
    private fun updateUI(FirebaseUser: FirebaseUser?) {
        if(FirebaseUser != null) {
//            val user = Teachers(FirebaseUser.uid, FirebaseUser.displayName)
//            val usersDao = TeacherDao()
//            usersDao.addTeacher(user)

            startActivity(Intent(this, LoginActivity :: class.java))
            finish()
        }
        else {
            val registerButton = findViewById<Button>(R.id.registerButton)
            val pbr = findViewById<ProgressBar>(R.id.progressBarRegister)
            pbr.visibility = View.GONE
            registerButton.visibility = View.VISIBLE
        }
    }
}
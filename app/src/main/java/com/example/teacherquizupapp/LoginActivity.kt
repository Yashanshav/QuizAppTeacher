package com.example.teacherquizupapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val tag = "Login Activity Tag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.loginEmail)
        val password = findViewById<EditText>(R.id.loginPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val register = findViewById<TextView>(R.id.signUp)
        val pbl = findViewById<ProgressBar>(R.id.progressBarLogin)

        auth = FirebaseAuth.getInstance()
        onStart()

        loginButton.setOnClickListener {

            register.visibility = View.GONE
            loginButton.visibility = View.GONE
            pbl.visibility = View.VISIBLE
            signIn(username.text.toString().trim(), password.text.toString().trim())

        }

        register.setOnClickListener {
            val registrationActivity = Intent(this, RegistrationActivity :: class.java)
            startActivity(registrationActivity)
            finish()
        }

    }

    private fun signIn(username: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(tag, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(tag, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(FirebaseUser: FirebaseUser?) {
        if(FirebaseUser != null) {
            val loginActivityIntent = Intent(this, MainActivity :: class.java)
            startActivity(loginActivityIntent)
            finish()
        }
        else {
            val loginButton = findViewById<Button>(R.id.loginButton)
            val pbl = findViewById<ProgressBar>(R.id.progressBarLogin)
            val register = findViewById<TextView>(R.id.signUp)
            pbl.visibility = View.GONE
            loginButton.visibility = View.VISIBLE
            register.visibility = View.VISIBLE
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI(currentUser)
        }
    }
}
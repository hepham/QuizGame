package com.example.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quizgame.databinding.ActivityQuizBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class QuizActivity : AppCompatActivity() {
    lateinit var quizBinding: ActivityQuizBinding
    val auth:FirebaseAuth= FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding= ActivityQuizBinding.inflate(layoutInflater)
        val view=quizBinding.root
        setContentView(view)
        quizBinding.btnQuizGame.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        quizBinding.btnLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()
            val googleSignInClient=GoogleSignIn.getClient(this,gso)
            googleSignInClient.signOut().addOnCompleteListener {task->

                if(task.isSuccessful){
                    Toast.makeText(applicationContext,"Sign out successful", Toast.LENGTH_SHORT).show()

                }else{

                    Toast.makeText(applicationContext,task.exception?.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            val intent= Intent(this@QuizActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
package com.example.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.quizgame.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    lateinit var signUpBinding: ActivitySignUpBinding
    val auth:FirebaseAuth= FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding= ActivitySignUpBinding.inflate(layoutInflater)
        val view=signUpBinding.root
        setContentView(view)

        signUpBinding.btnSignUp.setOnClickListener {

            val email=signUpBinding.signUpEmail.text.toString()
            val password=signUpBinding.signUpPassword.text.toString()

            signupWithFirebase(email, password)
        }
    }

    fun signupWithFirebase(email:String,password:String){
        signUpBinding.progressBarSignUp.visibility= View.VISIBLE
        signUpBinding.btnSignUp.isClickable=false

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{task->

            if (task.isSuccessful){

                Toast.makeText(applicationContext,"The account has been created",Toast.LENGTH_SHORT).show()
                finish()
                signUpBinding.progressBarSignUp.visibility=View.INVISIBLE
                signUpBinding.btnSignUp.isClickable=true

            }else{

                Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }
    }

}
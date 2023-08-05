package com.example.quizgame

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.quizgame.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding
    val auth:FirebaseAuth= FirebaseAuth.getInstance()
    lateinit var googleSignInClient:GoogleSignInClient
    lateinit var activityResultLaucher:ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding= ActivityLoginBinding.inflate(layoutInflater)
        val view=loginBinding.root
        setContentView(view)
        loginBinding.btnSignIn.setOnClickListener {
            val userEmail=loginBinding.loginEmail.text.toString()
            val userPassword=loginBinding.loginPassword.text.toString()
            signInUser(userEmail,userPassword)

        }
        val textOfGoogleButton=loginBinding.btnSignInWithGoogle.getChildAt(0) as TextView
        textOfGoogleButton.text="Continue with Google"
        textOfGoogleButton.setTextColor(Color.BLACK)
        textOfGoogleButton.textSize=16F
        registerActivityForGoogleSignIn()
        loginBinding.btnSignInWithGoogle.setOnClickListener {
            signInGoogle()
        }

        loginBinding.tvSignUp.setOnClickListener {
            val intent= Intent(this@LoginActivity,SignUpActivity::class.java)
            startActivity(intent)
        }

        loginBinding.tvForgotPassword.setOnClickListener {
            val intent= Intent(this@LoginActivity,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signInGoogle() {
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("126548932887-3v9n7prsq1booinup257j3ilk8nptp0d.apps.googleusercontent.com")
            .requestEmail().build()
        googleSignInClient=GoogleSignIn.getClient(this,gso)
        signIn()
    }

    private fun signIn() {
        val signInItent:Intent=googleSignInClient.signInIntent
        activityResultLaucher.launch(signInItent)
    }

    fun signInUser(userEmail:String,userPassword:String){
        auth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener {task ->

            if(task.isSuccessful){
                Toast.makeText(applicationContext,"Wellcome to Quiz Game",Toast.LENGTH_SHORT).show()
                val intent=Intent(this@LoginActivity,QuizActivity::class.java)
                startActivity(intent)
                finish()
            }else{

                Toast.makeText(applicationContext,task.exception?.toString(),Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val user=auth.currentUser
        if(user!=null){
            val intent=Intent(this@LoginActivity,QuizActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun registerActivityForGoogleSignIn(){
        activityResultLaucher=registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {result->
                val resultCode=result.resultCode
                val data=result.data
                if(resultCode== RESULT_OK&&data!=null){
                    val task: Task<GoogleSignInAccount> =GoogleSignIn.getSignedInAccountFromIntent(data)
                    fireBaseSignInWithGoogle(task)
                }

            })
    }



    private fun fireBaseSignInWithGoogle(task: Task<GoogleSignInAccount>) {
        try{
            val account:GoogleSignInAccount=task.getResult(ApiException::class.java)
            Toast.makeText(applicationContext,"Wellcome to Quiz Game",Toast.LENGTH_SHORT).show()
            val intent=Intent(this@LoginActivity,QuizActivity::class.java)
            startActivity(intent)
            finish()
            firebaseGoogleAccount(account)
        }catch (e:ApiException){
            Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_SHORT).show()
        }

    }
    private fun firebaseGoogleAccount(account: GoogleSignInAccount) {
        val authCredential=GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(authCredential).addOnCompleteListener { task->
            if(task.isSuccessful){
//                val user=auth.currentUser
            }
        }
    }

}
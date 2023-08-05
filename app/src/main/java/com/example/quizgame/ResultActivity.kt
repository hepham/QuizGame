package com.example.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizgame.databinding.ActivityResultBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ResultActivity : AppCompatActivity() {
    lateinit var resultBinding: ActivityResultBinding
    val database=FirebaseDatabase.getInstance()
    val databaseReference=database.reference.child("scores")
    val auth=FirebaseAuth.getInstance()
    val user=auth.currentUser
    var userCorrect=""
    var userWrong=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultBinding= ActivityResultBinding.inflate(layoutInflater)
        val view=resultBinding.root
        setContentView(view)
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user?.let {
                    val userId=it.uid
                    userCorrect=snapshot.child(userId).child("correct").value.toString()
                    userWrong=snapshot.child(userId).child("wrong").value.toString()
                    resultBinding.textViewcorrectCount.text=userCorrect
                    resultBinding.tvWrongAnswersCount.text=userWrong
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        resultBinding.buttonPlayAgain.setOnClickListener {
            val intent=Intent(this@ResultActivity,ResultActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
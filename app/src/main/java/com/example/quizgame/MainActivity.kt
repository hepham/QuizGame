package com.example.quizgame

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.quizgame.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var quizBinding: ActivityMainBinding
    val database = FirebaseDatabase.getInstance()
    val databaseReference=database.reference.child("Question")
    var question=""
    var answerA=""
    var answerB=""
    var answerC=""
    var answerD=""
    var correctAnswer=""
    var questionCount=0
    var questionNumber=1
    var userAnswer=""
    var userCorrect=0
    var userWrong=0
    lateinit var timer:CountDownTimer
    private  val totalTime=10000L
    var timeContinue=false
    var leftTime=totalTime
    val auth=FirebaseAuth.getInstance()
    val user=auth.currentUser
    val scorRef=database.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding= ActivityMainBinding.inflate(layoutInflater)
        val view=quizBinding.root
        setContentView(view)
        gameLogic()
        quizBinding.buttonNext.setOnClickListener {
            resetTimer()
            gameLogic()

        }

        quizBinding.buttonFinish.setOnClickListener {
            sendScore()

        }

        quizBinding.textViewA.setOnClickListener {

            pauseTimer()
            userAnswer="a"
            if(correctAnswer==userAnswer){
                quizBinding.textViewA.setBackgroundResource(R.drawable.correct_answer)
                userCorrect++
                quizBinding.textViewCorrectAnswers.text=userCorrect.toString()
            }else{
                quizBinding.textViewA.setBackgroundResource(R.drawable.wrong_answer)
                userWrong++
                quizBinding.textViewWrongAnswers.text=userWrong.toString()
                findAnswer()
            }
            disableClickableOfOptions()

        }

        quizBinding.textViewB.setOnClickListener {

            pauseTimer()
            userAnswer="b"
            if(correctAnswer==userAnswer){
                quizBinding.textViewB.setBackgroundResource(R.drawable.correct_answer)
                userCorrect++
                quizBinding.textViewCorrectAnswers.text=userCorrect.toString()
            }else{
                quizBinding.textViewB.setBackgroundResource(R.drawable.wrong_answer)
                userWrong++
                quizBinding.textViewWrongAnswers.text=userWrong.toString()
                findAnswer()
            }
            disableClickableOfOptions()
        }

        quizBinding.textViewC.setOnClickListener {

            pauseTimer()
            userAnswer="c"
            if(correctAnswer==userAnswer){
                quizBinding.textViewC.setBackgroundResource(R.drawable.correct_answer)
                userCorrect++
                quizBinding.textViewCorrectAnswers.text=userCorrect.toString()
            }else{
                quizBinding.textViewC.setBackgroundResource(R.drawable.wrong_answer)
                userWrong++
                quizBinding.textViewWrongAnswers.text=userWrong.toString()
                findAnswer()
            }
            disableClickableOfOptions()
        }

        quizBinding.textViewD.setOnClickListener {

            pauseTimer()
            userAnswer="d"
            if(correctAnswer==userAnswer){
                quizBinding.textViewD.setBackgroundResource(R.drawable.correct_answer)
                userCorrect++
                quizBinding.textViewCorrectAnswers.text=userCorrect.toString()
            }else{
                quizBinding.textViewD.setBackgroundResource(R.drawable.wrong_answer)
                userWrong++
                quizBinding.textViewWrongAnswers.text=userWrong.toString()
                findAnswer()
            }
            disableClickableOfOptions()
        }

    }


    private fun gameLogic(){
        restoreOptions()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                questionCount=snapshot.childrenCount.toInt()
                if(questionNumber<=questionCount){
                    question=snapshot.child(questionNumber.toString()).child("question").value.toString()
                    answerA=snapshot.child(questionNumber.toString()).child("a").value.toString()
                    answerB=snapshot.child(questionNumber.toString()).child("b").value.toString()
                    answerC=snapshot.child(questionNumber.toString()).child("c").value.toString()
                    answerD=snapshot.child(questionNumber.toString()).child("d").value.toString()
                    correctAnswer=snapshot.child(questionNumber.toString()).child("answer").value.toString()
                    println(question)
                    quizBinding.textViewQuestion.text=question
                    quizBinding.textViewA.text=answerA
                    quizBinding.textViewB.text=answerB
                    quizBinding.textViewC.text=answerC
                    quizBinding.textViewD.text=answerD

                    quizBinding.progressBarQuiz.visibility= View.INVISIBLE
                    quizBinding.linearLayoutInfor.visibility=View.VISIBLE
                    quizBinding.linearLayoutButton.visibility=View.VISIBLE
                    quizBinding.linearLayoutAnswer.visibility=View.VISIBLE
                    startTimer()

                }else{
                    val dialogMessage=AlertDialog.Builder(this@MainActivity)
                    dialogMessage.setTitle("Quiz Game")
                    dialogMessage.setMessage("Congratulation!!!\n You have answered all the questions. Do you want to see the result?")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("See Result"){dialogWindow,position->
                        sendScore()
                    }
                    dialogMessage.setNegativeButton("Play Again"){dialogWindow,position->
                        val intent=Intent(this@MainActivity,QuizActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialogMessage.create().show()
                }
                questionNumber++


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_LONG).show()
            }

        })
    }
    fun findAnswer(){
        when(correctAnswer){
            "a"->quizBinding.textViewA.setBackgroundResource(R.drawable.correct_answer)
            "b"->quizBinding.textViewB.setBackgroundResource(R.drawable.correct_answer)
            "c"->quizBinding.textViewC.setBackgroundResource(R.drawable.correct_answer)
            "d"->quizBinding.textViewD.setBackgroundResource(R.drawable.correct_answer)
        }
    }
    fun disableClickableOfOptions(){
        quizBinding.textViewA.isClickable=false
        quizBinding.textViewB.isClickable=false
        quizBinding.textViewC.isClickable=false
        quizBinding.textViewD.isClickable=false
    }
    fun restoreOptions(){
        quizBinding.textViewA.setBackgroundResource(R.drawable.answer_shape)
        quizBinding.textViewB.setBackgroundResource(R.drawable.answer_shape)
        quizBinding.textViewC.setBackgroundResource(R.drawable.answer_shape)
        quizBinding.textViewD.setBackgroundResource(R.drawable.answer_shape)
        quizBinding.textViewA.isClickable=true
        quizBinding.textViewB.isClickable=true
        quizBinding.textViewC.isClickable=true
        quizBinding.textViewD.isClickable=true

    }
    private fun startTimer(){
        timer=object :CountDownTimer(leftTime,1000){
            override fun onTick(milisUntilFinish: Long) {

                leftTime=milisUntilFinish
                updateCountDownText()
            }

            override fun onFinish() {
                println("vao day ne")
                disableClickableOfOptions()
                resetTimer()
                updateCountDownText()
                quizBinding.textViewQuestion.text="Sorry, Time is up! Continue with the next question"
                timeContinue=false
            }

        }.start()
        timeContinue=true
    }

    private fun resetTimer() {
        pauseTimer()
        leftTime=totalTime
        updateCountDownText()
    }
    fun pauseTimer(){
        timer.cancel()
        timeContinue=false
    }
    private fun updateCountDownText() {
        val remainingTimer:Int=(leftTime/1000).toInt()
        quizBinding.textViewTime.text=remainingTimer.toString()
    }
    fun sendScore(){
        user?.let {
            val userId=it.uid
            scorRef.child("scores").child(userId).child("correct").setValue(userCorrect)
            scorRef.child("scores").child(userId).child("wrong").setValue(userWrong).addOnSuccessListener {
                Toast.makeText(applicationContext,"Score sent to database successfully",Toast.LENGTH_LONG).show()
                val intent=Intent(this@MainActivity,ResultActivity::class.java)
                startActivity(intent)
                finish()
            }


        }
    }
}
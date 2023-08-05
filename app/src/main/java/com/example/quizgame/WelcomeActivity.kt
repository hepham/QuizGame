package com.example.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.example.quizgame.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    lateinit var welcomeBinding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        welcomeBinding= ActivityWelcomeBinding.inflate(layoutInflater)
        val view=welcomeBinding.root
        setContentView(view)
        val alphaAnimation=AnimationUtils.loadAnimation(applicationContext,R.anim.splash_anim)
        welcomeBinding.quizGame.startAnimation(alphaAnimation)
        val hander=Handler(Looper.getMainLooper())
        hander.postDelayed(object:Runnable{
            override fun run() {
                val intent= Intent(this@WelcomeActivity,LoginActivity::class.java)
                startActivity(intent)
            }
        },1000)
    }
}
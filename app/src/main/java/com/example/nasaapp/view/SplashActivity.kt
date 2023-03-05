package com.example.nasaapp.view

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.nasaapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val rotationValue = 720F
    private val durationValue = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.launcherForeground.animate()
            .rotation(rotationValue)
            .alpha(0f)
            .setInterpolator(LinearInterpolator())
            .setDuration(durationValue)
            .setListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator?) {
                }
                override fun onAnimationEnd(animation: Animator?) {
                    startActivity(Intent(this@SplashActivity, ActivityNasaApp::class.java))
                    finish()
                }
                override fun onAnimationCancel(animation: Animator?) {
                }
                override fun onAnimationRepeat(animation: Animator?) {
                }
            })
    }
}
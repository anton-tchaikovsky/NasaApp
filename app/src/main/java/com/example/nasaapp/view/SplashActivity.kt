package com.example.nasaapp.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.nasaapp.databinding.ActivitySplashBinding
import com.example.nasaapp.utils.DURATION

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val handler = Handler(Looper.getMainLooper())
    private val rotationValue = 540F
    private val durationValue = 5000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.launcherForeground.animate()
            .rotation(rotationValue)
            .setInterpolator(LinearInterpolator())
            .duration = durationValue

        handler.postDelayed ({
            startActivity(Intent(this, ActivityNasaApp::class.java))
            finish()
        }, DURATION
        )
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
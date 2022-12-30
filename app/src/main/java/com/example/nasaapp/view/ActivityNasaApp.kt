package com.example.nasaapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nasaapp.databinding.ActivityNasaAppBinding
import com.example.nasaapp.utils.TAG_ASTRONOMY_PICTURES_OF_THE_DAY_FRAGMENT


class ActivityNasaApp : AppCompatActivity() {

    private lateinit var binding: ActivityNasaAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNasaAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    binding.container.id,
                    AstronomyPicturesOfTheDayFragment.newInstance(),
                    TAG_ASTRONOMY_PICTURES_OF_THE_DAY_FRAGMENT
                )
                .commitAllowingStateLoss()
        }
    }


}
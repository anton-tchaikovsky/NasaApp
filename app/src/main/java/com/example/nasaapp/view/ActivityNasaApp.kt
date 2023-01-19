package com.example.nasaapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nasaapp.databinding.ActivityNasaAppBinding
import com.example.nasaapp.utils.*


class ActivityNasaApp : AppCompatActivity() {

    private lateinit var binding: ActivityNasaAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNasaAppBinding.inflate(layoutInflater)
        setTheme(getPreferences(MODE_PRIVATE).getString(KEY_SAVED_THEME, Theme.THEME_RED.title))
        setContentView(binding.root)
        if (savedInstanceState == null)
            createChoosingTheDayFragment()
        setListenerForChoosingTheme()
    }

    // метод создает ChoosingTheDayFragment
    private fun createChoosingTheDayFragment(){
            supportFragmentManager.beginTransaction()
                .replace(
                    binding.containerForChoosingTheDay.id,
                    ChoosingTheDayFragment.newInstance(),
                    TAG_CHOOSING_THE_DAY_FRAGMENT
                )
                .commitAllowingStateLoss()
    }

    // метод устанавливает тему приложения
    private fun setTheme(title:String?){
        when(title){
            Theme.THEME_RED.title -> setTheme(Theme.THEME_RED.id)
            Theme.THEME_BLUE.title -> setTheme(Theme.THEME_BLUE.id)
            Theme.THEME_ORANGE.title -> setTheme(Theme.THEME_ORANGE.id)
            else -> setTheme(Theme.THEME_RED.id)
        }
    }

    // метод сохраняет в настройки выбранную тему (если она не является текущей) и перезапускает активити
    private fun setListenerForChoosingTheme(){
        supportFragmentManager.setFragmentResultListener(
            KEY_CHOOSING_THEME, this
        ) { _, result ->
            result.getString(KEY_CHOSEN_THEME)?.let {
                getPreferences(MODE_PRIVATE).apply {
                    if(getString(KEY_SAVED_THEME, Theme.THEME_RED.title)!=it){
                        edit().putString(KEY_SAVED_THEME, it).apply()
                        recreate()
                    }
                }
            }
        }
    }

}
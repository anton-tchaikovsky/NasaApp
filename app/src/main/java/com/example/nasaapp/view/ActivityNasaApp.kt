package com.example.nasaapp.view

import android.content.res.Configuration
import android.os.Build
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        // обрабатываем изменение темы (светлая/темная) пользователем на устройстве
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO ->
                    showToast(this, LIGHT_THEME_ENABLED)
                Configuration.UI_MODE_NIGHT_YES ->
                    showToast(this, DARK_THEME_ENABLED)
            }
            recreate()
        }
        super.onConfigurationChanged(newConfig)
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
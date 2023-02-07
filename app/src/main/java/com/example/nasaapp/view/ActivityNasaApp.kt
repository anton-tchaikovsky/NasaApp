package com.example.nasaapp.view

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.nasaapp.R
import com.example.nasaapp.databinding.ActivityNasaAppBinding
import com.example.nasaapp.utils.*
import com.example.nasaapp.view_model.MarsRoverPhotosViewModel


class ActivityNasaApp : AppCompatActivity() {

    private lateinit var binding: ActivityNasaAppBinding

    private val viewModel: MarsRoverPhotosViewModel by lazy {
        ViewModelProvider(this)[MarsRoverPhotosViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getPreferences(MODE_PRIVATE).getString(KEY_SAVED_THEME, Theme.THEME_RED.title))
        super.onCreate(savedInstanceState)
        binding = ActivityNasaAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        settingNavigationView()
        if (savedInstanceState == null) {
            openChoosingTheDayFragment()
        }
        setListenerForChoosingTheme()
    }

    // метод настраивает меню NavigationView
    private fun settingNavigationView() {
        binding.navigationView.apply {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.hd -> (supportFragmentManager.findFragmentByTag(
                        TAG_CHOOSING_THE_DAY_FRAGMENT
                    ) as ChoosingTheDayFragment).run{
                        openHdAstronomyPicturesOfTheDayFragment()
                    }
                    R.id.setting -> openChoosingThemeFragment()
                    R.id.home -> (supportFragmentManager.findFragmentByTag(
                        TAG_CHOOSING_THE_DAY_FRAGMENT
                    ) as ChoosingTheDayFragment).run {
                        if (supportFragmentManager.backStackEntryCount > 0)
                            supportFragmentManager.popBackStack(
                                null,
                                FragmentManager.POP_BACK_STACK_INCLUSIVE
                            )
                        if (!hasAstronomyPicturesOfTheDay())
                            settingViewPager()
                    }
                    R.id.mars -> {
                        openViewPagerForMarsRoverPhotosFragment()
                        removeBadge(R.id.mars)
                    }
                    R.id.earth -> {
                        openEatherPhotosFragment()
                        removeBadge(R.id.earth)
                    }
                }
                true
            }
            setOnItemReselectedListener { item ->
                when (item.itemId) {
                    R.id.hd -> (supportFragmentManager.findFragmentByTag(
                        TAG_HD_ASTRONOMY_PICTURES_OF_THE_DAY_FRAGMENT
                    ) as HdAstronomyPicturesOfTheDayFragment).setHdAstronomyPicturesOfTheDay()
                    R.id.setting -> {}
                    R.id.home -> (supportFragmentManager.findFragmentByTag(
                        TAG_CHOOSING_THE_DAY_FRAGMENT
                    ) as ChoosingTheDayFragment).settingViewPager()
                    R.id.mars -> (supportFragmentManager.findFragmentByTag(
                      TAG_VIEW_PAGER_FOR_MARS_ROVER_PHOTOS_FRAGMENT
                  ) as ViewPagerForMarsRoverPhotosFragment).run {
                        if (hasMarsRoverPhotos())
                            updateItem()
                        else
                            getMarsRoverPhotos()
                    }
                    R.id.earth -> (supportFragmentManager.findFragmentByTag(
                        TAG_EARTH_PHOTOS_FRAGMENT
                    ) as EarthPhotosFragment).getEarthPhotos()
                }
            }
        }
        createBadgeEarth()
        createBadgeMars()
    }

    private fun createBadgeMars() {
        viewModel.run {
            getLiveDataCountMarsRoverPhotos().observe(this@ActivityNasaApp) {
                binding.navigationView.getOrCreateBadge(R.id.mars).apply {
                    if (it > 0)
                        number = it
                }
            }
            getCountMarsRoverPhotos(ViewPagerForMarsRoverPhotosFragment.day)
        }
    }

    private fun createBadgeEarth() {
        binding.navigationView.getOrCreateBadge(R.id.earth).apply {
            number = 1
        }
    }

    // метод открывает фрагмент EatherPhotosFragment
    private fun openEatherPhotosFragment() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.container_for_choosing_the_day,
                EarthPhotosFragment.newInstance(),
                TAG_EARTH_PHOTOS_FRAGMENT
            )
            .addToBackStack("")
            .commitAllowingStateLoss()
    }

    // метод открывает фрагмент ViewPagerMarsRoverPhotosFragment
    private fun openViewPagerForMarsRoverPhotosFragment() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.container_for_choosing_the_day,
                ViewPagerForMarsRoverPhotosFragment.newInstance(),
                TAG_VIEW_PAGER_FOR_MARS_ROVER_PHOTOS_FRAGMENT
            )
            .addToBackStack("")
            .commitAllowingStateLoss()
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

    override fun onBackPressed() {
        super.onBackPressed()
        bindBackStackWithNavigationView()
    }

    // метод выделяет элемент меню в соответствии с видимым на экране фрагментом (необходимо после работы popBackStack)
    // и перезагружает viewPager для AstronomyPicturesOfTheDay, если до этого не было данных
    private fun bindBackStackWithNavigationView() {
        binding.navigationView.menu.run {
            if (supportFragmentManager.backStackEntryCount == 0) {
                getItem(0).isChecked = true

                (supportFragmentManager.findFragmentByTag(
                    TAG_CHOOSING_THE_DAY_FRAGMENT
                ) as ChoosingTheDayFragment).run {
                    if (!hasAstronomyPicturesOfTheDay())
                        settingViewPager()
                }
            }
            else {
                when (supportFragmentManager.fragments.last().tag) {
                    TAG_HD_ASTRONOMY_PICTURES_OF_THE_DAY_FRAGMENT ->
                        getItem(1).isChecked = true
                    TAG_EARTH_PHOTOS_FRAGMENT ->
                        getItem(2).isChecked = true
                    TAG_VIEW_PAGER_FOR_MARS_ROVER_PHOTOS_FRAGMENT ->
                        getItem(3).isChecked = true
                    TAG_CHOOSING_THEME_FRAGMENT -> getItem(4).isChecked = true
                }
            }
        }
    }

    // метод создает ChoosingTheDayFragment
    private fun openChoosingTheDayFragment() {
            supportFragmentManager.beginTransaction()
                .replace(
                    binding.containerForChoosingTheDay.id,
                    ChoosingTheDayFragment.newInstance(),
                    TAG_CHOOSING_THE_DAY_FRAGMENT
                )
                .commitAllowingStateLoss()
    }

    // метод отображает фрагмент ChoosingThemeFragment
    private fun openChoosingThemeFragment() {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.container_for_choosing_the_day, ChoosingThemeFragment.newInstance(),
                    TAG_CHOOSING_THEME_FRAGMENT
                )
                .addToBackStack("")
                .commitAllowingStateLoss()
    }

    // метод устанавливает тему приложения
    private fun setTheme(title: String?) {
        when (title) {
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
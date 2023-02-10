package com.example.nasaapp.view

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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

    private var expandedToolbar = true

    override fun onSaveInstanceState(outState: Bundle) {
        // сохраняем информацию о текущем фрагменте
        outState.apply {
            putString(KEY_TAG_CURRENT_FRAGMENT, supportFragmentManager.fragments.last().tag)
            putBoolean(KEY_EXPANDED_TOOLBAR, expandedToolbar)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // получив информацию о текущем фрагменте, настраиваем toolbar и container для фрагментов
        savedInstanceState.run {
            binding.appBar.setExpanded(getBoolean(KEY_EXPANDED_TOOLBAR))
            getString(KEY_TAG_CURRENT_FRAGMENT)?.let {
                setTitleToolbar(it)
                if (it == TAG_CHOOSING_THEME_FRAGMENT) {
                    // изменяем margin_bottom для контейнера (т.к. скрывается NavigationView)
                    (binding.container.layoutParams as ViewGroup.MarginLayoutParams)
                        .setMargins(
                            0,
                            0,
                            0,
                            resources.getDimensionPixelSize(R.dimen.margin_bottom_container_48)
                        )
                    // скрываем NavigationView (показываем после закрытия фрагмента ChoosingThemeFragment)
                    binding.navigationView.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getPreferences(MODE_PRIVATE).getString(KEY_SAVED_THEME, Theme.THEME_RED.title))
        super.onCreate(savedInstanceState)
        binding = ActivityNasaAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        settingNavigationView()
        if (savedInstanceState == null) {
            openChoosingTheDayFragment()
        }
        setListenerForChoosingTheme()
        // вешаем слушатель на appBar для сохранения его текущего состояния (используется при пересоздании активити):
        binding.appBar.addOnOffsetChangedListener { _, verticalOffset ->
            expandedToolbar = verticalOffset == 0
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
                if (supportFragmentManager.fragments.last().tag != TAG_CHOOSING_THEME_FRAGMENT) {
                    openChoosingThemeFragment()
                    // изменяем margin_bottom для контейнера (т.к. скрывается NavigationView)
                    (binding.container.layoutParams as ViewGroup.MarginLayoutParams)
                        .setMargins(0, 0, 0, resources.getDimensionPixelSize(R.dimen.margin_bottom_container_48))
                    // скрываем NavigationView (показываем после закрытия фрагмента ChoosingThemeFragment)
                    binding.navigationView.visibility = View.GONE
                    true
                } else false
            }
            else -> false
        }
    }

    // метод настраивает меню NavigationView
    private fun settingNavigationView() {
        binding.navigationView.apply {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.hd -> (supportFragmentManager.findFragmentByTag(
                        TAG_CHOOSING_THE_DAY_FRAGMENT
                    ) as ChoosingTheDayFragment).run {
                        openHdAstronomyPicturesOfTheDayFragment()
                    }
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
                        setTitleToolbar(TAG_CHOOSING_THE_DAY_FRAGMENT)
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
                R.id.container,
                EarthPhotosFragment.newInstance(),
                TAG_EARTH_PHOTOS_FRAGMENT
            )
            .addToBackStack("")
            .commitAllowingStateLoss()
        setTitleToolbar(TAG_EARTH_PHOTOS_FRAGMENT)
    }

    // метод открывает фрагмент ViewPagerMarsRoverPhotosFragment
    private fun openViewPagerForMarsRoverPhotosFragment() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.container,
                ViewPagerForMarsRoverPhotosFragment.newInstance(),
                TAG_VIEW_PAGER_FOR_MARS_ROVER_PHOTOS_FRAGMENT
            )
            .addToBackStack("")
            .commitAllowingStateLoss()
        setTitleToolbar(TAG_VIEW_PAGER_FOR_MARS_ROVER_PHOTOS_FRAGMENT)
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
        if (supportFragmentManager.fragments.last().tag == TAG_CHOOSING_THEME_FRAGMENT) {
            // изменяем margin_bottom для контейнера (т.к. показывается NavigationView)
            (binding.container.layoutParams as ViewGroup.MarginLayoutParams)
                .setMargins(0, 0, 0, resources.getDimensionPixelSize(R.dimen.margin_bottom_container_96))
            // показываем navigationView, при закрытии фрагмента CHOOSING_THEME_FRAGMENT
            binding.navigationView.visibility = View.VISIBLE
        }
        super.onBackPressed()
        settingBackStack()
    }

    // метод выделяет элемент меню в соответствии с видимым на экране фрагментом (необходимо после работы popBackStack)
    // и перезагружает viewPager для AstronomyPicturesOfTheDay, если до этого не было данных
    // и устанавливает соответствующий title для toolbar
    private fun settingBackStack() {
        binding.navigationView.menu.run {
            if (supportFragmentManager.backStackEntryCount == 0) {
                getItem(0).isChecked = true
                (supportFragmentManager.findFragmentByTag(
                    TAG_CHOOSING_THE_DAY_FRAGMENT
                ) as ChoosingTheDayFragment).run {
                    if (!hasAstronomyPicturesOfTheDay())
                        settingViewPager()
                }
                setTitleToolbar(TAG_CHOOSING_THE_DAY_FRAGMENT)
            }
            else {
                when (supportFragmentManager.fragments.last().tag) {
                    TAG_HD_ASTRONOMY_PICTURES_OF_THE_DAY_FRAGMENT -> {
                        getItem(1).isChecked = true
                        setTitleToolbar(TAG_HD_ASTRONOMY_PICTURES_OF_THE_DAY_FRAGMENT)
                    }
                    TAG_EARTH_PHOTOS_FRAGMENT -> {
                        getItem(2).isChecked = true
                        setTitleToolbar(TAG_EARTH_PHOTOS_FRAGMENT)
                    }

                    TAG_VIEW_PAGER_FOR_MARS_ROVER_PHOTOS_FRAGMENT -> {
                        getItem(3).isChecked = true
                        setTitleToolbar(TAG_VIEW_PAGER_FOR_MARS_ROVER_PHOTOS_FRAGMENT)
                    }
                    TAG_CHOOSING_THEME_FRAGMENT ->
                        setTitleToolbar(TAG_CHOOSING_THEME_FRAGMENT)
                }
            }
        }
    }

    // метод создает ChoosingTheDayFragment
    private fun openChoosingTheDayFragment() {
            supportFragmentManager.beginTransaction()
                .replace(
                    binding.container.id,
                    ChoosingTheDayFragment.newInstance(),
                    TAG_CHOOSING_THE_DAY_FRAGMENT
                )
                .commitAllowingStateLoss()
        setTitleToolbar(TAG_CHOOSING_THE_DAY_FRAGMENT)
    }

    // метод отображает фрагмент ChoosingThemeFragment
    private fun openChoosingThemeFragment() {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.container, ChoosingThemeFragment.newInstance(),
                    TAG_CHOOSING_THEME_FRAGMENT
                )
                .addToBackStack("")
                .commitAllowingStateLoss()
        setTitleToolbar(TAG_CHOOSING_THEME_FRAGMENT)
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
                    if (getString(KEY_SAVED_THEME, Theme.THEME_RED.title) != it) {
                        edit().putString(KEY_SAVED_THEME, it).apply()
                        recreate()
                    }
                }
            }
        }
    }

    // метод устанавливает title для Toolbar по тегу фрагмента
    private fun setTitleToolbar(tagFragment: String) {
        binding.toolbarLayout.title =
            when (tagFragment) {
                TAG_CHOOSING_THE_DAY_FRAGMENT -> ASTRONOMY_PICTURE_OF_THE_DAY
                TAG_HD_ASTRONOMY_PICTURES_OF_THE_DAY_FRAGMENT -> ASTRONOMY_PICTURE_OF_THE_DAY
                TAG_EARTH_PHOTOS_FRAGMENT -> EARTH_PHOTO
                TAG_VIEW_PAGER_FOR_MARS_ROVER_PHOTOS_FRAGMENT -> MARS_ROVER_PHOTO
                TAG_CHOOSING_THEME_FRAGMENT -> CHOOSING_THEME
                else -> null
            }
    }

}
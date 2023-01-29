package com.example.nasaapp.view

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.nasaapp.R
import com.example.nasaapp.databinding.ChoosingTheDayLayoutBinding
import com.example.nasaapp.model.dto.AstronomyPictureOfTheDay
import com.example.nasaapp.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator

class ChoosingTheDayFragment:Fragment() {
    companion object {
        fun newInstance() = ChoosingTheDayFragment()
        fun selectedItemMenuHome(activity: Activity){
            activity.findViewById<BottomNavigationView>(R.id.navigation_view)?.let{
                it.selectedItemId = R.id.home
            }
        }
    }

    private var _binding: ChoosingTheDayLayoutBinding? = null
    private val binding get() = _binding!!

    private val listAstronomyPictureOfTheDay: MutableList<AstronomyPictureOfTheDay?> = mutableListOf(null, null, null)

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChoosingTheDayLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingSearchTextField()
        settingNavigationView()
        setListenerForCurrentAstronomyPicturesOfTheDay()
        settingViewPager(readChoosingDay())
    }

    private fun settingNavigationView() {
        requireActivity().findViewById<BottomNavigationView>(R.id.navigation_view)?.let { it ->
            it.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.hd -> openHdAstronomyPicturesOfTheDayFragment()
                    R.id.setting -> openChoosingThemeFragment()
                    R.id.home -> requireActivity().supportFragmentManager.popBackStack()
                }
                true
            }
            it.setOnItemReselectedListener {item ->
                when (item.itemId) {
                    R.id.hd -> (requireActivity().supportFragmentManager.findFragmentByTag(
                        TAG_HD_ASTRONOMY_PICTURES_OF_THE_DAY_FRAGMENT) as HdAstronomyPicturesOfTheDayFragment).setHdAstronomyPicturesOfTheDay()
                    R.id.setting ->{}
                    R.id.home -> settingViewPager(Day.values()[viewPager.currentItem])
                }
            }
        }
    }


    // метод получает объект DTO astronomyPictureOfTheDay для созданного фрагмента AstronomyPicturesOfTheDayFragment и сохраняет его в list
    private fun setListenerForCurrentAstronomyPicturesOfTheDay() {
        requireActivity().supportFragmentManager.run {
            Day.values().forEachIndexed { index, day ->
                setFragmentResultListener(
                    "$REQUIRE_KEY_ASTRONOMY_PICTURES_OF_THE_DAY ${day.day}",
                    viewLifecycleOwner
                ) { _, result ->
                    result.getParcelable<AstronomyPictureOfTheDay?>(
                        KEY_ASTRONOMY_PICTURES_OF_THE_DAY
                    ).let {
                        listAstronomyPictureOfTheDay[index] = it
                    }
                }
            }
        }
    }

    // метод настраивает ViewPager
    private fun settingViewPager(day: Day) {
       viewPager =  binding.containerForAstronomyPictureOfTheDay.apply {
            // привязываем адаптер к ViewPager
            adapter = ViewPagerAdapterForAstronomyPicturesOfTheDay(
                this@ChoosingTheDayFragment,
                Day.values()
            )
            // устанавливаем текущий фрагмент из сохраненных настроек
            currentItem = Day.values().indexOf(day)
        }
        settingTableLayout()
    }

    // метод связывает TableLayout и ViewPager и настраивает TableLayout
    private fun settingTableLayout(){
        TabLayoutMediator(binding.choosingLayout, viewPager
        ) { tab, position ->
            Day.values()[position].day.let {
                tab.text = it
                tab.contentDescription = it
            }
        }.attach()
    }

    // настройка поиска search_text_field
    private fun settingSearchTextField() {
        binding.searchTextField.apply {
            // слушатель нажатия на иконку поиска
            setStartIconOnClickListener {
                binding.searchEditText.text.toString().let {
                    if (it.length <= counterMaxLength)//проверка допустимой длинны введенного текста
                    // запрос на открытие браузера на устройстве
                       startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("$URI_YANDEX_SEARCH${it}")
                            )
                        )
                }
            }
            // слушатель ввода текста
            binding.searchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                // кастомная настройка ошибки при вводе недопустимо длинного текста
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    error = if (count > counterMaxLength)
                        getString(R.string.error_counter_input_text)
                    else null
                }
                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
    }

    // сохранение выбранного дня в настройках
    private fun saveChoosingDay(day: Day) {
        activity?.apply {
            getPreferences(MODE_PRIVATE).edit().putString(KEY_DAY,day.day).apply()
        }
    }

    // чтение выбранного дня из настроек
    private fun readChoosingDay(): Day {
        var day = Day.TODAY
        activity?.let {
            when (it.getPreferences(MODE_PRIVATE).getString(KEY_DAY, TODAY)) {
                TODAY -> day = Day.TODAY
                YESTERDAY -> day = Day.YESTERDAY
                DAY_BEFORE_YESTERDAY -> day = Day.DAY_BEFORE_YESTERDAY
            }
        }
        return day
    }

    // метод отображает соответствующий фрагмент HdAstronomyPicturesOfTheDay
    private fun openHdAstronomyPicturesOfTheDayFragment() {
        if (isConnectNetwork(context)) {
            listAstronomyPictureOfTheDay.getOrNull(viewPager.currentItem)?.let {
                requireActivity().supportFragmentManager.beginTransaction()
                    .add(
                        R.id.container_for_choosing_the_day,
                        HdAstronomyPicturesOfTheDayFragment.newInstance(it),
                        TAG_HD_ASTRONOMY_PICTURES_OF_THE_DAY_FRAGMENT
                    )
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        } else {
            showToast(context, DISCONNECT_NETWORK)
        }
    }

    // метод отображает фрагмент ChoosingThemeFragment
    private fun openChoosingThemeFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(
                R.id.container_for_choosing_the_day, ChoosingThemeFragment.newInstance(),
                TAG_CHOOSING_THEME_FRAGMENT
            )
            .addToBackStack("")
            .commitAllowingStateLoss()
    }

    override fun onPause() {
        saveChoosingDay(Day.values()[viewPager.currentItem])
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
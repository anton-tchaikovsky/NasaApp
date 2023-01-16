package com.example.nasaapp.view

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
import com.example.nasaapp.R
import com.example.nasaapp.databinding.ChoosingTheDayLayoutBinding
import com.example.nasaapp.model.dto.AstronomyPictureOfTheDay
import com.example.nasaapp.utils.*

class ChoosingTheDayFragment:Fragment() {
    companion object {
        fun newInstance() = ChoosingTheDayFragment()
    }

    private var _binding: ChoosingTheDayLayoutBinding? = null
    private val binding get() = _binding!!

    private var astronomyPictureOfTheDay:AstronomyPictureOfTheDay? = null

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
        setOnClickListenerForChips()
        settingSearchTextField()
        readChoosingDay().let {
            checkedChoosingDay(it)
            createAstronomyPicturesOfTheDayFragment(it)
        }
        setOnClickListenerForFAB()
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

    // создаем фрагмент AstronomyPicturesOfTheDay
    private fun createAstronomyPicturesOfTheDayFragment(day: Day) {
        requireActivity().supportFragmentManager.run {
            // отображаем фрагмент AstronomyPicturesOfTheDay
            beginTransaction()
                .replace(
                    binding.containerForAstronomyPictureOfTheDay.id,
                    AstronomyPicturesOfTheDayFragment.newInstance(day),
                    TAG_ASTRONOMY_PICTURES_OF_THE_DAY_FRAGMENT
                )
                .commitAllowingStateLoss()
            // вешаем слушателя для получения результата из AstronomyPicturesOfTheDay
            setFragmentResultListener(
                REQUIRE_KEY_ASTRONOMY_PICTURES_OF_THE_DAY, viewLifecycleOwner
            ) { _, result -> result.getParcelable<AstronomyPictureOfTheDay>(KEY_ASTRONOMY_PICTURES_OF_THE_DAY).let{
                astronomyPictureOfTheDay = it
            }}
        }
    }

    // сохранение выбранного дня в настройках
    private fun saveChoosingDay(day: Day) {
        activity?.apply {
            getPreferences(MODE_PRIVATE).edit().putString(KEY_DAY,day.day).apply()
        }
    }

    // чтение выбранного дня из настроек
    private fun readChoosingDay(): Day{
        var day = Day.TODAY
        activity?.let{
            when (it.getPreferences(MODE_PRIVATE).getString(KEY_DAY, TODAY)){
                TODAY -> day=Day.TODAY
                YESTERDAY -> day=Day.YESTERDAY
                DAY_BEFORE_YESTERDAY -> day = Day.DAY_BEFORE_YESTERDAY
            }
        }
        return day
    }

    // метод устанавливает выбранный chip на основании сохраненнго в настройках (используется при запуске ChoosingTheDayFragment)
    private fun checkedChoosingDay(day: Day){
        binding.run {
            when(day){
                Day.TODAY -> today.isChecked = true
                Day.YESTERDAY -> yesterday.isChecked = true
                Day.DAY_BEFORE_YESTERDAY -> dayBeforeYesterday.isChecked = true
            }
        }
    }

    // метод устанавливает слушателей на chips для отображения соответствующего AstronomyPicturesOfTheDay по нажатию и сохранения выбранного дня
    private fun setOnClickListenerForChips(){
        binding.run {
            today.setOnClickListener{
                saveChoosingDay(Day.TODAY)
                createAstronomyPicturesOfTheDayFragment(Day.TODAY)
                fabHd.extend()
            }
            yesterday.setOnClickListener {
                saveChoosingDay(Day.YESTERDAY)
                createAstronomyPicturesOfTheDayFragment(Day.YESTERDAY)
                fabHd.extend()
            }
            dayBeforeYesterday.setOnClickListener {
                saveChoosingDay(Day.DAY_BEFORE_YESTERDAY)
                createAstronomyPicturesOfTheDayFragment(Day.DAY_BEFORE_YESTERDAY)
                fabHd.extend()
            }
        }
    }

    // метод устанавливает слушателя на FAB, для отображения соответствующего HdAstronomyPicturesOfTheDay
    private fun setOnClickListenerForFAB(){
        binding.fabHd.setOnClickListener {
            if(isConnectNetwork(context)){
                astronomyPictureOfTheDay?.let {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .add(R.id.container_for_choosing_the_day, HdAstronomyPicturesOfTheDayFragment.newInstance(it),
                            TAG_HD_ASTRONOMY_PICTURES_OF_THE_DAY_FRAGMENT)
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
            } else{
                showToast(context, DISCONNECT_NETWORK)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
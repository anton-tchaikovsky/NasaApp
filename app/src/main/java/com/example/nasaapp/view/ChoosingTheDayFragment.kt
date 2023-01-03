package com.example.nasaapp.view

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nasaapp.databinding.ChoosingTheDayLayoutBinding
import com.example.nasaapp.utils.*

class ChoosingTheDayFragment:Fragment() {
    companion object {
        const val KEY_DAY = "Day"
        fun newInstance() = ChoosingTheDayFragment()
    }

    private var _binding: ChoosingTheDayLayoutBinding? = null
    private val binding get() = _binding!!

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
        readChoosingDay().let {
            createAstronomyPicturesOfTheDayFragment(it)
            checkedChoosingDay(it)
        }
    }

    // отображение фрагмента AstronomyPicturesOfTheDay
    private fun createAstronomyPicturesOfTheDayFragment(day:Day){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(binding.containerForAstronomyPictureOfTheDay.id, AstronomyPicturesOfTheDayFragment.newInstance(day), TAG_ASTRONOMY_PICTURES_OF_THE_DAY_FRAGMENT)
            .commitAllowingStateLoss()
    }

    // сохранение выбранного дня в настройках
    private fun saveChoosingDay(day: Day){
        activity?.apply {
            getPreferences(MODE_PRIVATE).edit().putString(KEY_DAY,day.day).apply()
        }
    }

    // чтение выбранного дня из настроек
    private fun readChoosingDay(): Day{
        var day = Day.TODAY
        activity?.let{
            when (it.getPreferences(MODE_PRIVATE).getString(DAY, TODAY)){
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
            }
            yesterday.setOnClickListener {
                saveChoosingDay(Day.YESTERDAY)
                createAstronomyPicturesOfTheDayFragment(Day.YESTERDAY)
            }
            dayBeforeYesterday.setOnClickListener {
                saveChoosingDay(Day.DAY_BEFORE_YESTERDAY)
                createAstronomyPicturesOfTheDayFragment(Day.DAY_BEFORE_YESTERDAY)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
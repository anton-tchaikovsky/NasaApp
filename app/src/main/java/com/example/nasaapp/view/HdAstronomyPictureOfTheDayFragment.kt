package com.example.nasaapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import coil.api.load
import com.example.nasaapp.R
import com.example.nasaapp.databinding.HdAstronomyPictureOfTheDayFragmentBinding
import com.example.nasaapp.model.dto.AstronomyPictureOfTheDay
import com.google.android.material.bottomsheet.BottomSheetBehavior

class HdAstronomyPicturesOfTheDayFragment : Fragment() {
    companion object {
        private const val KEY_ASTRONOMY_PICTURE_OF_THE_DAY = "KeyAstronomyPictureOfTheDay"
        fun newInstance(astronomyPictureOfTheDay: AstronomyPictureOfTheDay): HdAstronomyPicturesOfTheDayFragment =
            HdAstronomyPicturesOfTheDayFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_ASTRONOMY_PICTURE_OF_THE_DAY, astronomyPictureOfTheDay)
                }
            }
    }

    private var _binding: HdAstronomyPictureOfTheDayFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HdAstronomyPictureOfTheDayFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            it.getParcelable<AstronomyPictureOfTheDay>(KEY_ASTRONOMY_PICTURE_OF_THE_DAY)
                ?.let { astronomy_pictures_of_the_day ->
                    setHdAstronomyPicturesOfTheDay(astronomy_pictures_of_the_day)
                }
        }
        startingSettingBottomSheetBehavior(binding.bottomSheetLayout)
        // вешаем на fab_back popBackStack
        binding.fabBack.setOnClickListener { activity?.supportFragmentManager?.popBackStack() }
    }

    // метод устанавливает исходное состояние BottomSheet и устанавливает на него слушателя нажатия
    private fun startingSettingBottomSheetBehavior(bottomSheetView: LinearLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED // изначально "выглядывает"
        }
        binding.bottomSheetLayout.setOnClickListener {
            @SuppressLint("SwitchIntDef")
            when (bottomSheetBehavior.state) {
                BottomSheetBehavior.STATE_COLLAPSED -> bottomSheetBehavior.state =
                    BottomSheetBehavior.STATE_EXPANDED
                BottomSheetBehavior.STATE_EXPANDED -> bottomSheetBehavior.state =
                    BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun setHdAstronomyPicturesOfTheDay(astronomyPictureOfTheDay: AstronomyPictureOfTheDay) {
        astronomyPictureOfTheDay.apply {
            setPicture(hdurl)
            setDescription(title, explanation)
        }
    }

    private fun setDescription(title: String, explanation: String) {
        binding.run {
            this@run.title.text = title
            this@run.explanation.text = explanation
        }
    }

    private fun setPicture(urlPicture: String) {
        binding.hdPicture.load(urlPicture) {
            lifecycle(this@HdAstronomyPicturesOfTheDayFragment)
            error(R.drawable.ic_baseline_file_download_off_24)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
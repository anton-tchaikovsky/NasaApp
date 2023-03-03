package com.example.nasaapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.transition.Fade
import androidx.transition.TransitionManager
import coil.api.load
import com.example.nasaapp.R
import com.example.nasaapp.databinding.HdAstronomyPictureOfTheDayFragmentBinding
import com.example.nasaapp.model.dto.AstronomyPictureOfTheDay
import com.example.nasaapp.utils.DURATION
import com.example.nasaapp.utils.Theme
import com.example.nasaapp.utils.hideShowViews
import com.example.nasaapp.utils.prepareMenu
import com.google.android.material.bottomsheet.BottomSheetBehavior

class HdAstronomyPicturesOfTheDayFragment : Fragment() {
    companion object {
        private const val KEY_ASTRONOMY_PICTURE_OF_THE_DAY = "KeyAstronomyPictureOfTheDay"
        fun newInstance(astronomyPictureOfTheDay: AstronomyPictureOfTheDay?): HdAstronomyPicturesOfTheDayFragment =
            HdAstronomyPicturesOfTheDayFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_ASTRONOMY_PICTURE_OF_THE_DAY, astronomyPictureOfTheDay)
                }
            }
    }

    private var _binding: HdAstronomyPictureOfTheDayFragmentBinding? = null
    private val binding get() = _binding!!

    private var astronomyPictureOfTheDay:AstronomyPictureOfTheDay? = null

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

        // вешаем на fab_back popBackStack
        binding.fabBack.setOnClickListener { requireActivity().onBackPressed() }
        settingBottomSheetBehavior(binding.bottomSheetLayout)
        arguments?.let { bundle ->
            bundle.getParcelable<AstronomyPictureOfTheDay>(KEY_ASTRONOMY_PICTURE_OF_THE_DAY)
                .let {
                    astronomyPictureOfTheDay = it
                    setHdAstronomyPicturesOfTheDay(it)
                }
        }
        prepareMenu(requireActivity() as ActivityNasaApp, viewLifecycleOwner)
    }

    // метод настраивает BottomSheet и устанавливает на него слушателя нажатия
    private fun settingBottomSheetBehavior(bottomSheetView: LinearLayout) {
        // получаем текущую тему
        val outValue = TypedValue()
        requireActivity().theme.resolveAttribute(android.R.attr.name, outValue, true)
        val themeTitle = outValue.string.toString()

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED // изначально "выглядывает"
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    @SuppressLint("SwitchIntDef")
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_EXPANDED){
                        when (themeTitle){
                            Theme.THEME_RED.title ->bottomSheetView.setBackgroundColor(ContextCompat.getColor(requireContext(), Theme.THEME_RED.idColorSurface))
                            Theme.THEME_BLUE.title ->bottomSheetView.setBackgroundColor(ContextCompat.getColor(requireContext(), Theme.THEME_BLUE.idColorSurface))
                            Theme.THEME_ORANGE.title ->bottomSheetView.setBackgroundColor(ContextCompat.getColor(requireContext(), Theme.THEME_ORANGE.idColorSurface))
                        }
                    }
                }
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    when (themeTitle){
                        Theme.THEME_RED.title ->bottomSheetView.setBackgroundColor(ContextCompat.getColor(requireContext(), Theme.THEME_RED.idColorPrimary))
                        Theme.THEME_BLUE.title ->bottomSheetView.setBackgroundColor(ContextCompat.getColor(requireContext(), Theme.THEME_BLUE.idColorPrimary))
                        Theme.THEME_ORANGE.title ->bottomSheetView.setBackgroundColor(ContextCompat.getColor(requireContext(), Theme.THEME_ORANGE.idColorPrimary))
                    }
                }
            })
        }
        bottomSheetView.setOnClickListener {
            @SuppressLint("SwitchIntDef")
            when (bottomSheetBehavior.state) {
                BottomSheetBehavior.STATE_COLLAPSED -> bottomSheetBehavior.state =
                    BottomSheetBehavior.STATE_EXPANDED
                BottomSheetBehavior.STATE_EXPANDED -> bottomSheetBehavior.state =
                    BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun setHdAstronomyPicturesOfTheDay(astronomyPictureOfTheDay: AstronomyPictureOfTheDay?) {
        if(astronomyPictureOfTheDay!=null){
            astronomyPictureOfTheDay.apply {
                setPicture(hdurl)
                setDescription(title, explanation)
            }
        } else
            setError()
    }

    fun setHdAstronomyPicturesOfTheDay(){
        setHdAstronomyPicturesOfTheDay(astronomyPictureOfTheDay)
    }

    private fun setError() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.fabBack.updateLayoutParams <ViewGroup.MarginLayoutParams> {
            setMargins(0,0, resources.getDimension(R.dimen.margin_end_fab_back).toInt(), resources.getDimension(R.dimen.margin_bottom_fab_back_24).toInt())
        }
        hideShowViews(
            listOf(binding.hdPicture, binding.loadingLayout.loadingLayout),
            listOf(binding.loadingError.loadingError)
        )
    }

    private fun setDescription(title: String, explanation: String) {
        AstronomyPicturesOfTheDayFragment.setTitle(requireActivity(), requireContext(), binding.title, title)
        AstronomyPicturesOfTheDayFragment.setExplanation(requireActivity(), binding.explanation, explanation)
    }

    private fun setPicture(urlPicture: String) {
        binding.hdPicture.load(urlPicture) {
            lifecycle(this@HdAstronomyPicturesOfTheDayFragment)

            listener(
                onStart = {
                    binding.run {
                        hideShowViews(
                            listOf(loadingError.loadingError, hdPicture),
                            listOf(loadingLayout.loadingLayout)
                        )
                    }
                },
                onSuccess = { _, _ ->
                    binding.run {
                        TransitionManager.beginDelayedTransition(containerForHdAstronomyPictureOfTheDay, Fade(Fade.IN).apply {
                            duration = DURATION
                        })
                        hideShowViews(
                            listOf(loadingError.loadingError, loadingLayout.loadingLayout),
                            listOf(hdPicture)
                        )
                    }
                },
                onError = { _, _ ->
                    binding.run {
                        hideShowViews(
                            listOf(hdPicture, loadingLayout.loadingLayout),
                            listOf(loadingError.loadingError)
                        )
                    }
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
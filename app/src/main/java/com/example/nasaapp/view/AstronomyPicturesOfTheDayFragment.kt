package com.example.nasaapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Fade
import androidx.transition.TransitionManager
import coil.api.load
import coil.transform.RoundedCornersTransformation
import com.example.nasaapp.R
import com.example.nasaapp.databinding.AstronomyPictureOfTheDayFragmentBinding
import com.example.nasaapp.model.dto.AstronomyPictureOfTheDay
import com.example.nasaapp.utils.*
import com.example.nasaapp.view_model.AppStateAstronomyPicturesOfTheDay
import com.example.nasaapp.view_model.AstronomyPicturesOfTheDayViewModel
import com.google.android.material.tabs.TabLayout

class AstronomyPicturesOfTheDayFragment : Fragment() {

    companion object {
        fun newInstance(day: Day): AstronomyPicturesOfTheDayFragment =
            AstronomyPicturesOfTheDayFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_DAY, day)
                }
            }
    }

    private var _binding: AstronomyPictureOfTheDayFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var day:Day

    private val viewModel: AstronomyPicturesOfTheDayViewModel by lazy {
        ViewModelProvider(this)[AstronomyPicturesOfTheDayViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AstronomyPictureOfTheDayFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }
        arguments?.let {
            day = it.getSerializable(KEY_DAY) as Day
            viewModel.getAstronomyPicturesOfTheDay(day)
        }
        // вешаем слушатель на scroll для изменения elevation TabLayout в ChoosingTheDayFragment
        binding.scrollForTitlePictureDescription.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            requireActivity().findViewById<TabLayout>(
                R.id.choosing_layout
            ).isSelected = v.canScrollVertically(-1)
        })
    }

    private fun renderData(appState: AppStateAstronomyPicturesOfTheDay) {
        when (appState) {
            is AppStateAstronomyPicturesOfTheDay.Error -> setError(appState.error)
            AppStateAstronomyPicturesOfTheDay.Loading -> setLoading()
            is AppStateAstronomyPicturesOfTheDay.Success -> setAstronomyPictureOfTheDay(appState.astronomyPicturesOfTheDay)
        }
    }

    private fun setError(throwable: Throwable) {
        binding.run {
            hideShowViews(
                listOf(loadingLayout.loadingLayout, groupAstronomyPicturesOfTheDay),
                listOf(loadingError.loadingError)
            )
        }
        if (!isConnectNetwork(requireContext()))
            showToast(context, DISCONNECT_NETWORK)
        else {
            showToast(context, LOADING_ERROR)
            if (DEBUG)
                Log.v(TAG_APP, throwable.toString())
        }
        setResultForChoosingTheDayFragment(null)
    }

    private fun setLoading() {
        binding.run {
            hideShowViews(
                listOf(loadingError.loadingError, groupAstronomyPicturesOfTheDay),
                listOf(loadingLayout.loadingLayout)
            )
        }
    }

    private fun setAstronomyPictureOfTheDay(astronomyPicturesOfTheDay: AstronomyPictureOfTheDay) {
        binding.run {
            TransitionManager.beginDelayedTransition(containerForTitlePictureDescription, Fade(Fade.IN).apply {
                duration = DURATION
            })
            hideShowViews(
                listOf(loadingLayout.loadingLayout, loadingError.loadingError),
                listOf(this@run.groupAstronomyPicturesOfTheDay)
            )
        }
        astronomyPicturesOfTheDay.apply {
            setPicture(url)
            setDescription(title, explanation)
            setResultForChoosingTheDayFragment(this)
        }
    }

    private fun setDescription(title: String, explanation: String) {
        binding.run {
            this@run.title.text = title
            this@run.explanation.text = explanation
        }
    }

    private fun setPicture(urlPicture: String) {
        binding.image.load(urlPicture) {
            lifecycle(this@AstronomyPicturesOfTheDayFragment)
            transformations(RoundedCornersTransformation(40f))
            listener(
                onStart = {
                    binding.run{
                        hideShowViews(
                            listOf(loadingErrorPicture.loadingError, image),
                            listOf(loadingLayoutPicture.loadingLayout)
                        )
                    }
                },
                onSuccess = {_, _ ->
                    binding.run {
                        hideShowViews(
                            listOf(loadingErrorPicture.loadingError, loadingLayoutPicture.loadingLayout),
                            listOf(image)
                        )
                    }
                },
                onError = { _, _ ->
                    binding.run {
                        hideShowViews(
                            listOf(image, loadingLayoutPicture.loadingLayout),
                            listOf(loadingErrorPicture.loadingError)
                        )
                    }
                }
            )
        }
    }

    private fun setResultForChoosingTheDayFragment(astronomyPictureOfTheDay: AstronomyPictureOfTheDay?) {
        requireActivity().supportFragmentManager.setFragmentResult(
            "$REQUIRE_KEY_ASTRONOMY_PICTURES_OF_THE_DAY ${day.day}",
            Bundle().apply { putParcelable(KEY_ASTRONOMY_PICTURES_OF_THE_DAY, astronomyPictureOfTheDay) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
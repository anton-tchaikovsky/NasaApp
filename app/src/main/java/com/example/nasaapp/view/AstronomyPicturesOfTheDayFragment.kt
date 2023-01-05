package com.example.nasaapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import coil.transform.RoundedCornersTransformation
import com.example.nasaapp.R
import com.example.nasaapp.databinding.AstronomyPictureOfTheDayFragmentBinding
import com.example.nasaapp.model.dto.AstronomyPictureOfTheDay
import com.example.nasaapp.utils.*
import com.example.nasaapp.view_model.AppStateAstronomyPicturesOfTheDay
import com.example.nasaapp.view_model.AstronomyPicturesOfTheDayViewModel


class AstronomyPicturesOfTheDayFragment : Fragment() {

    companion object {
        fun newInstance(day:Day):AstronomyPicturesOfTheDayFragment =
            AstronomyPicturesOfTheDayFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(DAY,day)
                }
            }
    }

    private var _binding: AstronomyPictureOfTheDayFragmentBinding? = null
    private val binding get() = _binding!!

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
           viewModel.getAstronomyPicturesOfTheDay(it.getSerializable(DAY) as Day)
       }

    }

    private fun renderData(appState: AppStateAstronomyPicturesOfTheDay) {
        when (appState) {
            is AppStateAstronomyPicturesOfTheDay.Error -> setError(appState.error)
            AppStateAstronomyPicturesOfTheDay.Loading -> setLoading()
            is AppStateAstronomyPicturesOfTheDay.Success -> setAstronomyPicturesOfTheDay(appState.astronomyPicturesOfTheDay)
        }
    }

    private fun setError(throwable: Throwable){
        binding.run {
            hideShowViews(listOf(loadingLayout.loadingLayout,astronomyPicturesOfTheDay), listOf(loadingError))
        }
        if(!isConnectNetwork(requireContext()))
            Toast.makeText(requireContext(), DISCONNECT_NETWORK, Toast.LENGTH_SHORT).show()
        else{
            Toast.makeText(requireContext(), LOADING_ERROR, Toast.LENGTH_SHORT).show()
            Log.v(TAG_ERROR_APP, throwable.toString())
        }
    }

    private fun setLoading(){
        binding.run {
            hideShowViews(listOf(loadingError,astronomyPicturesOfTheDay), listOf(loadingLayout.loadingLayout))
        }
    }

    private fun setAstronomyPicturesOfTheDay(astronomyPicturesOfTheDay: AstronomyPictureOfTheDay) {
        binding.run {
            hideShowViews(listOf(loadingLayout.loadingLayout,loadingError), listOf(this@run.astronomyPicturesOfTheDay))
        }
        astronomyPicturesOfTheDay.let {
            setPicture(it.url)
            setExplanation(it.explanation)
        }
    }

    private fun setExplanation(explanation: String) {
        binding.explanation.text = explanation
    }

    private fun setPicture(urlPicture: String) {
        binding.picture.load(urlPicture){
            lifecycle(this@AstronomyPicturesOfTheDayFragment)
            transformations(RoundedCornersTransformation(40f))
            error(R.drawable.ic_baseline_file_download_off_24)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
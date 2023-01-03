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
            AppStateAstronomyPicturesOfTheDay.Loading -> showLoading()
            is AppStateAstronomyPicturesOfTheDay.Success -> setAstronomyPicturesOfTheDay(appState.astronomyPicturesOfTheDay)
        }
    }

    private fun setError(throwable: Throwable){
        hideLoading()
        if(!isConnectNetwork(requireContext()))
            Toast.makeText(requireContext(), DISCONNECT_NETWORK, Toast.LENGTH_SHORT).show()
        else{
            Toast.makeText(requireContext(), LOADING_ERROR, Toast.LENGTH_SHORT).show()
            Log.v(TAG_ERROR_APP, throwable.toString())
        }

    }

    private fun showLoading(){
        binding.run {
            astronomyPicturesOfTheDay.visibility = View.GONE
            loadingLayout.loadingLayout.visibility = View.VISIBLE
        }
    }

    private fun hideLoading(){
        binding.run {
            astronomyPicturesOfTheDay.visibility = View.VISIBLE
            loadingLayout.loadingLayout.visibility = View.GONE
        }
    }

    private fun setAstronomyPicturesOfTheDay(astronomyPicturesOfTheDay: AstronomyPictureOfTheDay) {
        hideLoading()
        astronomyPicturesOfTheDay.let {
            setPicture(it.url)
            setExplanation(it.explanation)
        }
    }

    private fun setExplanation(explanation: String) {
        binding.explanation.text = explanation
    }

    private fun setPicture(urlPicture: String) {
        binding.picture.load(urlPicture)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
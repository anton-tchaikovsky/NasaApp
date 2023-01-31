package com.example.nasaapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.nasaapp.R
import com.example.nasaapp.databinding.MarsRoverPhotosFragmentBinding
import com.example.nasaapp.model.dto.mars.MarsRoverPhotos
import com.example.nasaapp.utils.*
import com.example.nasaapp.view_model.AppStateMarsRoverPhotos
import com.example.nasaapp.view_model.MarsRoverPhotosViewModel


class MarsRoverPhotosFragment : Fragment() {

    companion object {
        fun newInstance(day: Day): MarsRoverPhotosFragment =
            MarsRoverPhotosFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_DAY, day)
                }
            }
    }

    private var _binding: MarsRoverPhotosFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MarsRoverPhotosViewModel by lazy {
        ViewModelProvider(this)[MarsRoverPhotosViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MarsRoverPhotosFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // вешаем на fab_back popBackStack
        binding.fabMarsBack.setOnClickListener { requireActivity().onBackPressed() }
        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }
        arguments?.let {
            viewModel.getMarsRoverPhotos(it.getSerializable(KEY_DAY) as Day)
        }
    }

    private fun renderData(appState: AppStateMarsRoverPhotos) {
        when (appState) {
            is AppStateMarsRoverPhotos.Error -> setError(appState.error)
            AppStateMarsRoverPhotos.Loading -> setLoading()
            is AppStateMarsRoverPhotos.Success -> setMarsRoverPhotos(appState.marsRoverPhotos)
        }
    }

    private fun setError(throwable: Throwable) {
        binding.run {
            hideShowViews(
                listOf(loadingLayout.loadingLayout, marsRoverPhoto),
                listOf(loadingError)
            )
        }
        if (!isConnectNetwork(requireContext()))
            showToast(context, DISCONNECT_NETWORK)
        else {
            showToast(context, LOADING_ERROR)
            if (DEBUG)
                Log.v(TAG_APP, throwable.toString())
        }
    }

    private fun setLoading() {
        binding.run {
            hideShowViews(
                listOf(loadingError, marsRoverPhoto),
                listOf(loadingLayout.loadingLayout)
            )
        }
    }

    private fun setMarsRoverPhotos(marsRoverPhotos: MarsRoverPhotos) {
        binding.run {
            hideShowViews(
                listOf(loadingLayout.loadingLayout, loadingError),
                listOf(this@run.marsRoverPhoto)
            )
        }
        marsRoverPhotos.apply {
            setPicture(photos[0].imgSrc)
        }
    }

    private fun setPicture(urlPicture: String) {
        binding.marsRoverPhoto.load(urlPicture) {
            lifecycle(this@MarsRoverPhotosFragment)
            error(R.drawable.ic_baseline_file_download_off_24)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
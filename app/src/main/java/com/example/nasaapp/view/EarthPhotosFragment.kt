package com.example.nasaapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.nasaapp.BuildConfig
import com.example.nasaapp.R
import com.example.nasaapp.databinding.EarthPhotosFragmentBinding
import com.example.nasaapp.model.dto.earth.EarthPolychromaticImagingCamera
import com.example.nasaapp.model.dto.earth.EarthPolychromaticImagingCameraItem
import com.example.nasaapp.utils.*
import com.example.nasaapp.view_model.AppStateEarthPhotos
import com.example.nasaapp.view_model.EarthPhotosViewModel
import com.google.android.material.appbar.CollapsingToolbarLayout


class EarthPhotosFragment : Fragment() {

    companion object {
        fun newInstance(): EarthPhotosFragment = EarthPhotosFragment()
    }

    private var _binding: EarthPhotosFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EarthPhotosViewModel by lazy {
        ViewModelProvider(this)[EarthPhotosViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EarthPhotosFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }
        getEarthPhotos()
    }

    private fun renderData(appState: AppStateEarthPhotos) {
        when (appState) {
            is AppStateEarthPhotos.Error -> setError(appState.error)
            AppStateEarthPhotos.Loading -> setLoading()
            is AppStateEarthPhotos.Success -> setEarthPhotos(appState.earthPolychromaticImagingCamera)
        }
    }

    fun getEarthPhotos() {
        viewModel.getEarthPhotos()
    }

    private fun setError(throwable: Throwable) {
        binding.run {
            hideShowViews(
                listOf(loadingLayout.loadingLayout, earthPhoto),
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
    }

    private fun setLoading() {
        binding.run {
            hideShowViews(
                listOf(loadingError.loadingError, earthPhoto),
                listOf(loadingLayout.loadingLayout)
            )
        }
    }

    private fun setEarthPhotos(earthPolychromaticImagingCamera: EarthPolychromaticImagingCamera) {
        binding.run {
            hideShowViews(
                listOf(loadingError.loadingError),
                listOf(this@run.earthPhoto)
            )
        }
        setPicture(getUrlPicture(earthPolychromaticImagingCamera.last()))
    }

    private fun getUrlPicture(earthPolychromaticImagingCameraItem: EarthPolychromaticImagingCameraItem): String {
        val date = earthPolychromaticImagingCameraItem.date.split(" ").first()
        val photoName = earthPolychromaticImagingCameraItem.image
        return String.format(
            URL_EARTH_PHOTO,
            (date.replace("-", "/")),
            photoName,
            BuildConfig.api_nasa_key
        )
    }

    private fun setPicture(urlPicture: String) {
        binding.earthPhoto.load(urlPicture) {
            lifecycle(this@EarthPhotosFragment)
            listener (
                onStart = {
                    binding.run {
                        hideShowViews(
                            listOf(loadingError.loadingError, earthPhoto),
                            listOf(loadingLayout.loadingLayout)
                        )
                    }
                },
                onSuccess = {_, _ ->
                    binding.run {
                        hideShowViews(
                            listOf(loadingError.loadingError, loadingLayout.loadingLayout),
                            listOf(earthPhoto)
                        )
                    }
                },
                onError = {_, _ ->
                    binding.run {
                        hideShowViews(
                            listOf(earthPhoto, loadingLayout.loadingLayout),
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
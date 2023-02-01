package com.example.nasaapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.api.load
import com.example.nasaapp.databinding.MarsPhotosFragmentBinding
import com.example.nasaapp.utils.KEY_URL_MARS_ROVER_PHOTO
import com.example.nasaapp.utils.hideShowViews


class MarsRoverPhotosFragment : Fragment() {

    companion object {
        fun newInstance(urlPicture: String): MarsRoverPhotosFragment =
            MarsRoverPhotosFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_URL_MARS_ROVER_PHOTO, urlPicture)
                }
            }
    }

    private var _binding: MarsPhotosFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MarsPhotosFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {bundle ->
            bundle.getString(KEY_URL_MARS_ROVER_PHOTO)?.let { setPicture(it) }
        }
    }

    private fun setPicture(urlPicture: String) {
        binding.photo.load(urlPicture) {
            lifecycle(this@MarsRoverPhotosFragment)
            listener(
                    onStart = {
                        binding.run {
                            hideShowViews(
                                listOf(loadingError.loadingError, photo),
                                listOf(loadingLayout.loadingLayout)
                            )
                        }
                    },
                    onSuccess = {_, _ ->
                        binding.run {
                            hideShowViews(
                                listOf(loadingError.loadingError, loadingLayout.loadingLayout),
                                listOf(photo)
                            )
                        }
                    },
                    onError = {_, _ ->
                        binding.run {
                            hideShowViews(
                                listOf(photo, loadingLayout.loadingLayout),
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
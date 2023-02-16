package com.example.nasaapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.Fade
import androidx.transition.TransitionManager
import coil.api.load
import com.example.nasaapp.databinding.MarsPhotosFragmentBinding
import com.example.nasaapp.model.dto.mars.Photo
import com.example.nasaapp.utils.DURATION
import com.example.nasaapp.utils.KEY_MARS_ROVER_PHOTO
import com.example.nasaapp.utils.hideShowViews


class MarsRoverPhotosFragment : Fragment() {

    companion object {
        fun newInstance(marsPhoto: Photo): MarsRoverPhotosFragment =
            MarsRoverPhotosFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_MARS_ROVER_PHOTO, marsPhoto)
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
            bundle.getParcelable<Photo>(KEY_MARS_ROVER_PHOTO)?.let { setMarsPhoto(it) }
        }
    }

    private fun setMarsPhoto(marsPhoto: Photo) {
        binding.marsPhoto.load(marsPhoto.imgSrc) {
            lifecycle(this@MarsRoverPhotosFragment)
            listener(
                    onStart = {
                        binding.run {
                            hideShowViews(
                                listOf(loadingError.loadingError, groupMarsPhoto),
                                listOf(loadingLayout.loadingLayout)
                            )
                        }
                    },
                    onSuccess = {_, _ ->
                        binding.run {
                            camera.text = marsPhoto.camera.fullName
                            earthDate.text = marsPhoto.earthDate
                            TransitionManager.beginDelayedTransition(containerForMarsPhoto, Fade(
                                Fade.IN).apply {
                                duration = DURATION
                            })
                            hideShowViews(
                                listOf(loadingError.loadingError, loadingLayout.loadingLayout),
                                listOf(groupMarsPhoto)
                            )
                        }
                    },
                    onError = {_, _ ->
                        binding.run {
                            hideShowViews(
                                listOf(groupMarsPhoto, loadingLayout.loadingLayout),
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
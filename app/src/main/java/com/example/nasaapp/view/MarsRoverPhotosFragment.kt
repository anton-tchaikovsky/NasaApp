package com.example.nasaapp.view

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.transition.*
import coil.api.load
import com.example.nasaapp.R
import com.example.nasaapp.databinding.MarsPhotosFragmentBinding
import com.example.nasaapp.model.dto.mars.Photo
import com.example.nasaapp.utils.DURATION
import com.example.nasaapp.utils.KEY_MARS_ROVER_PHOTO
import com.example.nasaapp.utils.hideShowViews


class MarsRoverPhotosFragment : BaseFragment<MarsPhotosFragmentBinding> (MarsPhotosFragmentBinding::inflate) {

    companion object {
        fun newInstance(marsPhoto: Photo): MarsRoverPhotosFragment =
            MarsRoverPhotosFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_MARS_ROVER_PHOTO, marsPhoto)
                }
            }

        // метод возвращает SpannableString, в котором 'o' преобразовано в изображение
        fun spannableStringForCamera(context: Context, nameCamera:String): SpannableString =
            SpannableString(nameCamera).apply {
                val bitmapCamera = ContextCompat.getDrawable(context, R.drawable.ic_round_camera_24)
                    ?.toBitmap()
                bitmapCamera?.let {
                    for (i in nameCamera.indices){
                        if (nameCamera[i]=='o')
                            setSpan(ImageSpan(context,it), i, i+1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
            }
    }

    // слушатель на mars_photo
    private val marsAnimatorListener = View.OnClickListener {
        val constraintSetZoom = ConstraintSet().apply { clone(context, R.layout.mars_photos_zoom) }
        TransitionManager.beginDelayedTransition(binding.containerForMarsPhoto,
            TransitionSet()
            .addTransition(ChangeBounds().apply {
                interpolator = AnticipateOvershootInterpolator(1.0f)
            })
            .addTransition(ChangeImageTransform())
            .setDuration(DURATION))
        constraintSetZoom.applyTo(binding.containerForMarsPhoto)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.marsPhoto.setOnClickListener(marsAnimatorListener)
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
                            camera.text = spannableStringForCamera(requireContext(), marsPhoto.camera.fullName)
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

}
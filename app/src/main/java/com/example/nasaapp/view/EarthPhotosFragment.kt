package com.example.nasaapp.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.*
import coil.api.load
import com.example.nasaapp.BuildConfig
import com.example.nasaapp.databinding.EarthPhotosFragmentBinding
import com.example.nasaapp.model.dto.earth.EarthPolychromaticImagingCamera
import com.example.nasaapp.model.dto.earth.EarthPolychromaticImagingCameraItem
import com.example.nasaapp.utils.*
import com.example.nasaapp.view_model.AppStateEarthPhotos
import com.example.nasaapp.view_model.EarthPhotosViewModel


class EarthPhotosFragment : Fragment() {

    companion object {
        fun newInstance(): EarthPhotosFragment = EarthPhotosFragment()
    }

    private var _binding: EarthPhotosFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EarthPhotosViewModel by lazy {
        ViewModelProvider(this)[EarthPhotosViewModel::class.java]
    }

    private var isOpenEarthMenu = false

    // слушатель на fab
    private val earthMenuAnimationListener:View.OnClickListener by lazy{
        View.OnClickListener{
            if (!isOpenEarthMenu){
                isOpenEarthMenu = true
                // вращение иконки
                ObjectAnimator.ofFloat(binding.iconEarthMenu, View.ROTATION, 0f, 405f)
                        .setDuration(DURATION)
                        .start()
                // изменение прозрачности и кликабельности фото
                binding.earthPhoto.animate()
                    .alpha(0.5f)
                    .setDuration(DURATION)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            binding.earthPhoto.isClickable = false
                        }
                    })
                    .start()

                // перемещение меню
                TransitionManager.beginDelayedTransition(binding.containerForEarthPhoto, ChangeBounds().setDuration(DURATION))
                binding.guideLineMenuHorizontal.run {
                    val layoutParams = layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.guidePercent = 0.5f
                    this.layoutParams = layoutParams
                }

            } else{
                isOpenEarthMenu = false
                // вращение иконки
                ObjectAnimator.ofFloat(binding.iconEarthMenu, View.ROTATION, 405f, 0f)
                    .setDuration(DURATION)
                    .start()
                // изменение прозрачности и кликабельности фото
                binding.earthPhoto.animate()
                    .alpha(1f)
                    .setDuration(DURATION)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            binding.earthPhoto.isClickable = true
                        }
                    })
                    .start()
                // перемещение меню
                TransitionManager.beginDelayedTransition(binding.containerForEarthPhoto, ChangeBounds().setDuration(DURATION))
                binding.guideLineMenuHorizontal.run {
                    val layoutParams = layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.guidePercent = 1f
                    this.layoutParams = layoutParams
                }
            }

        }
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
        prepareMenu(requireActivity() as ActivityNasaApp, viewLifecycleOwner)
        binding.fabEarthPhoto.setOnClickListener(earthMenuAnimationListener)
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
                        settingAnimationFade()
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

    private fun settingAnimationFade(){
        val transitionSet = TransitionSet()
            .addTransition(Fade(
                Fade.IN).apply {
                duration = DURATION
            })
            .addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition) {
                    super.onTransitionEnd(transition)
                    settingAnimationChangeBounds()
                }
                })
        TransitionManager.beginDelayedTransition(binding.containerForEarthPhoto, transitionSet)
    }

    private fun settingAnimationChangeBounds() {
        binding.earthPhoto.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.containerForEarthPhoto,
            TransitionSet()
                .addTransition(ChangeBounds())
                .addTransition(ChangeImageTransform())
                .setDuration(DURATION)
            )
            val layoutParams: ConstraintLayout.LayoutParams = it.layoutParams as ConstraintLayout.LayoutParams
            if ((it as AppCompatImageView).scaleType == ImageView.ScaleType.FIT_CENTER){
                it.scaleType = ImageView.ScaleType.CENTER_CROP
                layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
            } else{
                it.scaleType = ImageView.ScaleType.FIT_CENTER
                layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT
            }
            it.layoutParams = layoutParams
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
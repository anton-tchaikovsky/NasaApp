package com.example.nasaapp.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.nasaapp.databinding.ViewPagerForMarsRoverPhotosFragmentBinding
import com.example.nasaapp.model.dto.mars.MarsRoverPhotos
import com.example.nasaapp.utils.*
import com.example.nasaapp.view_model.AppStateMarsRoverPhotos
import com.example.nasaapp.view_model.MarsRoverPhotosViewModel


class ViewPagerForMarsRoverPhotosFragment : BaseFragment<ViewPagerForMarsRoverPhotosFragmentBinding> (ViewPagerForMarsRoverPhotosFragmentBinding::inflate) {

    companion object {
        fun newInstance(): ViewPagerForMarsRoverPhotosFragment =
            ViewPagerForMarsRoverPhotosFragment()
        val day = Day.DAY_BEFORE_YESTERDAY
    }

    private val viewModel: MarsRoverPhotosViewModel by lazy {
        ViewModelProvider(this)[MarsRoverPhotosViewModel::class.java]
    }

    private lateinit var viewPager: ViewPager2
    private lateinit var adapterForMarsRoverPhotos:ViewPagerAdapterForMarsRoverPhotos

     private var marsRoverPhotos: MarsRoverPhotos?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }
        getMarsRoverPhotos()
        prepareMenu(requireActivity() as ActivityNasaApp, viewLifecycleOwner)
    }

    fun getMarsRoverPhotos(){
         viewModel.getMarsRoverPhotos(day)
    }

    fun hasMarsRoverPhotos() = marsRoverPhotos!=null

    private fun renderData(appState: AppStateMarsRoverPhotos) {
        when (appState) {
            is AppStateMarsRoverPhotos.Error -> {
                marsRoverPhotos=null
                setError(appState.error)
            }
            AppStateMarsRoverPhotos.Loading -> setLoading()
            is AppStateMarsRoverPhotos.Success -> {
                marsRoverPhotos = appState.marsRoverPhotos
                marsRoverPhotos?.let {settingViewPager(it)}
            }
        }
    }

    private fun setError(throwable: Throwable) {
        binding.run {
            hideShowViews(
                listOf(loadingLayout.loadingLayout, containerForMarsRoverPhotos),
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
                listOf(loadingError.loadingError, containerForMarsRoverPhotos),
                listOf(loadingLayout.loadingLayout)
            )
        }
    }

   // метод настраивает ViewPager
    private fun settingViewPager(marsRoverPhotos: MarsRoverPhotos) {
        binding.run {
            hideShowViews(
                listOf(loadingError.loadingError, loadingLayout.loadingLayout),
                listOf(containerForMarsRoverPhotos)
            )
        }
        viewPager = binding.containerForMarsRoverPhotos.apply {
            adapterForMarsRoverPhotos = ViewPagerAdapterForMarsRoverPhotos(this@ViewPagerForMarsRoverPhotosFragment,marsRoverPhotos)
            adapter = adapterForMarsRoverPhotos //ViewPagerAdapterForMarsRoverPhotos(this@ViewPagerForMarsRoverPhotosFragment,marsRoverPhotos)
        }
    }

    // метод обнавляет текущий фрагмент
    fun updateItem(){
        adapterForMarsRoverPhotos.notifyItemChanged(viewPager.currentItem)
    }

}
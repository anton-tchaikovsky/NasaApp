package com.example.nasaapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nasaapp.R
import com.example.nasaapp.databinding.BottomMenuNavigationDrawerLayoutBinding
import com.example.nasaapp.utils.showToast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomMenuNavigationDrawerFragment : BottomSheetDialogFragment() {
    companion object {
        fun newInstance() = BottomMenuNavigationDrawerFragment()
    }

    private var _binding: BottomMenuNavigationDrawerLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomMenuNavigationDrawerLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.save -> showToast(context, it.title.toString())
                R.id.send -> showToast(context, it.title.toString())
            }
            dismiss()
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
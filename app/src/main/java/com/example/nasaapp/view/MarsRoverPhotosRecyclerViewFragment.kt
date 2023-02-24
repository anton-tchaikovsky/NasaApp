package com.example.nasaapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nasaapp.databinding.MarsPhotosRecyclerViewFragmentBinding
import com.example.nasaapp.view_model.MarsRoverPhotosViewModel


class MarsRoverPhotosRecyclerViewFragment : Fragment() {

    companion object {
        fun newInstance(): MarsRoverPhotosRecyclerViewFragment =
            MarsRoverPhotosRecyclerViewFragment()
    }

    private var _binding: MarsPhotosRecyclerViewFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MarsRoverPhotosViewModel by lazy {
        ViewModelProvider(this)[MarsRoverPhotosViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = MarsPhotosRecyclerViewFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.marsPhotosRecyclerView.adapter = AdapterForMarsRoverPhotosRecyclerViewFragment(viewModel.getListPhoto())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
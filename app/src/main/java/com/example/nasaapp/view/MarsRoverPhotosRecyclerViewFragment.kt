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

   private lateinit var adapter: AdapterForMarsRoverPhotosRecyclerViewFragment

    interface AddRemove{
        fun addItem(position: Int)
        fun removeItem(position: Int)
    }

    private val addRemoveCallback:AddRemove = object : AddRemove{
        override fun addItem(position: Int) {
           adapter.setListPhotoAfterAdd(viewModel.addPhoto(position), position)
        }

        override fun removeItem(position: Int) {
            adapter.setListPhotoAfterRemove(viewModel.addPhoto(position), position)
        }
    }

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
        adapter = AdapterForMarsRoverPhotosRecyclerViewFragment(viewModel.getListPhoto(),addRemoveCallback)
        binding.marsPhotosRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
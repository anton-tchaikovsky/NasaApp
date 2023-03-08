package com.example.nasaapp.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.nasaapp.databinding.MarsPhotosRecyclerViewFragmentBinding
import com.example.nasaapp.view_model.MarsRoverPhotosViewModel


class MarsRoverPhotosRecyclerViewFragment : BaseFragment<MarsPhotosRecyclerViewFragmentBinding>(MarsPhotosRecyclerViewFragmentBinding::inflate) {

    companion object {
        fun newInstance(): MarsRoverPhotosRecyclerViewFragment =
            MarsRoverPhotosRecyclerViewFragment()
    }

   private lateinit var adapter: AdapterForMarsRoverPhotosRecyclerViewFragment

    interface AddRemoveMove{
        fun addItem(position: Int)
        fun removeItem(position: Int)
        fun moveItem (fromPosition: Int, toPosition: Int)
    }

    interface ItemTouchHelperAdapter{
        fun moveItem(fromPosition: Int, toPosition: Int)
        fun dismissItem(position:Int)
    }

    interface ItemTouchHelperViewHolder{
        fun selectedItem(position: Int)
        fun unSelectedItem()
    }

    private val callback:AddRemoveMove = object : AddRemoveMove{
        override fun addItem(position: Int) {
           adapter.setListPhotoAfterAdd(viewModel.addPhoto(position), position)
        }

        override fun removeItem(position: Int) {
            adapter.setListPhotoAfterRemove(viewModel.removePhoto(position), position)
        }

        override fun moveItem(fromPosition: Int, toPosition: Int) {
            adapter.setListPhotoAfterMove(viewModel.movePhoto(fromPosition, toPosition), fromPosition, toPosition)
        }
    }

    class ItemTouchHelperCallback(val callback: ItemTouchHelperAdapter):ItemTouchHelper.Callback(){
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return makeMovementFlags(ItemTouchHelper.DOWN or ItemTouchHelper.UP, ItemTouchHelper.END)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            callback.moveItem(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            callback.dismissItem(viewHolder.adapterPosition)
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (viewHolder is AdapterForMarsRoverPhotosRecyclerViewFragment.MarsViewHolder)
                viewHolder.selectedItem(actionState)
            super.onSelectedChanged(viewHolder, actionState)
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            if (viewHolder is AdapterForMarsRoverPhotosRecyclerViewFragment.MarsViewHolder)
                viewHolder.unSelectedItem()
            super.clearView(recyclerView, viewHolder)
        }

    }

    private val viewModel: MarsRoverPhotosViewModel by lazy {
        ViewModelProvider(this)[MarsRoverPhotosViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AdapterForMarsRoverPhotosRecyclerViewFragment(viewModel.getListPhoto(),callback)
        binding.marsPhotosRecyclerView.adapter = adapter
        ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(binding.marsPhotosRecyclerView)
    }

}
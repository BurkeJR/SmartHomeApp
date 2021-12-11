package com.example.smarthomeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeapp.databinding.DoorItemBinding

class DiffCallbackDoors : DiffUtil.ItemCallback<door>() {
    override fun areItemsTheSame(oldItem: door, newItem: door): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: door, newItem: door): Boolean {
        return oldItem == newItem
    }
}

class DoorsAdapter (
    var doorList: List<door>,
    var onItemClick: ((door) -> Unit)? = null
): ListAdapter<door, DoorsAdapter.ViewHolder>(DiffCallbackDoors()) {
    inner class ViewHolder(private val item: DoorItemBinding) :RecyclerView.ViewHolder(item.root) {
            fun bind(index: Int, data: door){
                item.doorItemName.text = data.name
            }
            init{
                itemView.setOnClickListener{
                    onItemClick?.invoke(doorList[adapterPosition])
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DoorItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoorsAdapter.ViewHolder, position: Int) {
        holder.bind(position, getItem(position))

    }

    override fun getItemCount(): Int {
        return doorList.size
    }
}
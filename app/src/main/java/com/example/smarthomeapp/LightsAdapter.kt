package com.example.smarthomeapp



import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.widget.AdapterView.OnItemClickListener
import androidx.navigation.Navigation
import com.example.smarthomeapp.databinding.LightItemBinding

class DiffCallback : DiffUtil.ItemCallback<light>() {
    override fun areItemsTheSame(oldItem: light, newItem: light): Boolean = oldItem == newItem


    override fun areContentsTheSame(oldItem: light, newItem: light): Boolean = oldItem == newItem
}



class LightsAdapter: ListAdapter<light, LightsAdapter.ViewHolder>(DiffCallback()){

    var onToggleLight: ((light) -> Unit)? = null

    inner class ViewHolder(private val item: LightItemBinding) : RecyclerView.ViewHolder(item.root){

        fun bind(index: Int, data: light){
            item.lightSwitch.text = data.name
            item.lightSwitch.isChecked = data.isOn

            item.lightSwitch.setOnClickListener {
                onToggleLight?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LightItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, getItem(position))

    }
}
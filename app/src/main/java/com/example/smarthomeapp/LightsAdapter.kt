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

class DiffCallback : DiffUtil.ItemCallback<lights>() {
    override fun areItemsTheSame(oldItem: lights, newItem: lights): Boolean = oldItem == newItem


    override fun areContentsTheSame(oldItem: lights, newItem: lights): Boolean = oldItem == newItem
}



class LightsAdapter(
    var lightsList: List<lights>,
    var onItemClick: ((lights) -> Unit)? = null):
        ListAdapter<lights, LightsAdapter.ViewHolder>(DiffCallback()){


    inner class ViewHolder(private val item: LightItemBinding) : RecyclerView.ViewHolder(item.root){

        fun bind(index: Int, data: lights){
            item.lightSwitch.text = data.name
        }
        init {
            itemView.setOnClickListener{
                onItemClick?.invoke(lightsList[adapterPosition])
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

    override fun getItemCount(): Int {
        return lightsList.size
    }
}
package com.example.smarthomeapp



import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.widget.AdapterView.OnItemClickListener
import androidx.navigation.Navigation
import com.example.smarthomeapp.databinding.MediaListItemBinding


class DiffCallback : DiffUtil.ItemCallback<Media>() {
    override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean = oldItem == newItem


    override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean = oldItem == newItem
}



class MediaAdapter(
    var mediaList: List<Media>,
    var onItemClick: ((Media) -> Unit)? = null
): ListAdapter<Media, MediaAdapter.ViewHolder>(DiffCallback()){


    inner class ViewHolder(private val item: MediaListItemBinding) : RecyclerView.ViewHolder(item.root){

        fun bind(index: Int, data: Media){
            item.mediaNameText.text = data.name
        }
        init {
            itemView.setOnClickListener{
                onItemClick?.invoke(mediaList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MediaListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, getItem(position))

    }

    override fun getItemCount(): Int {
        return mediaList.size
    }
}
package com.example.smarthomeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomeapp.databinding.MediaListItemBinding

class DiffCallbackMedia : DiffUtil.ItemCallback<mediaPlayer>() {
    override fun areItemsTheSame(oldItem: mediaPlayer, newItem: mediaPlayer): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: mediaPlayer, newItem: mediaPlayer): Boolean = oldItem == newItem
}

class MediaAdapter(
    var mediaList: List<mediaPlayer>,
    var onItemClick: ((mediaPlayer) -> Unit)? = null
): ListAdapter<mediaPlayer, MediaAdapter.ViewHolder>(DiffCallbackMedia()){


    inner class ViewHolder(private val item: MediaListItemBinding) : RecyclerView.ViewHolder(item.root){

        fun bind(index: Int, data: mediaPlayer){
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
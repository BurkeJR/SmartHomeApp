package com.example.smarthomeapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smarthomeapp.databinding.FragmentMediaListBinding

class MediaFragment : Fragment() {
    private lateinit var binding: FragmentMediaListBinding
    private lateinit var adapter: MediaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaListBinding.inflate(inflater)

        var mediaList = mutableListOf(
            Media(0, "TV", false, 0,0),
            Media(1, "Google Home", false, 0,0)
        )


        adapter = MediaAdapter(mediaList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.submitList(mediaList)
        adapter.onItemClick = {
            val action = MediaFragmentDirections.actionMediaFragmentToMediaDetailsFragment(it.name, it.id)
            findNavController().navigate(action)
        }



        return binding.root
    }

}

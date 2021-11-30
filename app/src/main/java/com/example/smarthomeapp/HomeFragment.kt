package com.example.smarthomeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.smarthomeapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)



        binding.DoorButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_doorsFragment)
        }
        binding.LightButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_lightsFragment)
        }
        binding.MediaButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mediaFragment)
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}
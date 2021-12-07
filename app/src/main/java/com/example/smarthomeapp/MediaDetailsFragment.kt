package com.example.smarthomeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.smarthomeapp.databinding.FragmentMediaDetailsBinding
import com.example.smarthomeapp.databinding.FragmentMediaListBinding


class MediaDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMediaDetailsBinding
    val args: MediaDetailsFragmentArgs by navArgs<MediaDetailsFragmentArgs>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaDetailsBinding.inflate(inflater)
        //Set binding
        binding.mediaDetailTitleText.text = args.mediaName


        return binding.root
    }

}
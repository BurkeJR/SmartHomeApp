package com.example.smarthomeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.smarthomeapp.databinding.FragmentDoorsDetailsBinding


class DoorsDetailsFragment : Fragment() {

//    private lateinit var binding: FragmentDoorsDetailsBinding
//
//    private lateinit var requestQueue: RequestQueue
//    val args: DoorsDetailsFragmentArgs by navArgs<DoorsDetailsFragmentArgs>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        binding = FragmentDoorsDetailsBinding.inflate(inflater)
//
//        binding.doorDetailTitleText.text = args.doorName
//
//        requestQueue = Volley.newRequestQueue(this.context)
//
//        val ipAddress = getString(R.string.myIPAddress)
//        val url = "http://$ipAddress/doors?id="
//
//        if (!args.isMotorized)
//            binding.doorDetailsDoorSwitch.visibility = View.GONE
//
//        if (args.isOpen) {
//            binding.doorDetailsOpenStatusText.text = "Door is Open"
//        }else {
//            binding.doorDetailsOpenStatusText.text = "Door is Closed"
//        }
//
//        binding.doorDetailsDoorSwitch.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked){
//
//            }
//            else{
//
//            }
//        }
//
//        return binding.root
//    }

}
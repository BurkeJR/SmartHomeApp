package com.example.smarthomeapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.smarthomeapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    var netCon = false

    init {
        if (checkConnection(this)) {
            netCon = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        if (netCon){
            binding.ConnectStatus.text = "Connected!"
        }
        else{
            binding.ConnectStatus.text = "Not connected, check internet access to continue"
        }

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

    private fun checkConnection(context: Context): Boolean{
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network = connectivityManager.activeNetwork?: return false
            val activeNetwork = connectivityManager
                .getNetworkCapabilities(network) ?: return false

            return when{
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                else -> false
            }
        }
        else{
            return false
        }
    }

}
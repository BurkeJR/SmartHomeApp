package com.example.smarthomeapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.smarthomeapp.databinding.FragmentHomeBinding
import com.google.gson.Gson


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    var netCon = false

    private lateinit var requestQueue: RequestQueue


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        netCon = checkConnection(requireContext())

        if (netCon){
            binding.ConnectStatus.text = "Connected!"
            binding.DoorButton.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_doorsFragment)
            }
            binding.LightButton.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_lightsFragment)
            }
            binding.MediaButton.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_mediaFragment)
            }

            binding.bedtimeButton.setOnClickListener {
                requestQueue = Volley.newRequestQueue(this.context)
                val ipAddress = getString(R.string.myIPAddress)
                val doorUrl = "http://$ipAddress/doors"
                val lightsUrl = "http://$ipAddress/lights"
                val mediaUrl = "http://$ipAddress/media-players"

                lateinit var doorList: List<door>
                lateinit var openDoorList: List<door>
                lateinit var lightList: List<light>
                lateinit var mediaList: List<mediaPlayer>

                val doorRequest = StringRequest(
                    Request.Method.GET,
                    doorUrl,
                    {
                        val gson = Gson()

                        doorList = gson.fromJson<ArrayResult<door>>(it).result

                        for (door in doorList){
                            if (door.isOpen){
                                if (door.motorized) {
                                    door.isOpen = !door.isOpen
                                    val turnOffRequest = StringRequestWithBody(
                                        doorUrl + "?id=${door.id}",
                                        door,
                                        {},
                                        {})
                                    turnOffRequest.tag = this
                                    requestQueue.add(turnOffRequest)
                                }else{
//                                    TODO: add opened non-motorized doors to openDoorList
                                }
                            }
                        }

                    },{
                        Log.e("Error", "Request failed")
                    }
                )

                val lightRequest = StringRequest(
                    Request.Method.GET,
                    lightsUrl,
                    { thing ->
                        val gson = Gson()

                        lightList = gson.fromJson<ArrayResult<light>>(thing).result

                        for (light in lightList) {
                            if (light.isOn) {
                                light.isOn = !light.isOn

                                val turnOffRequest =
                                    StringRequestWithBody(lightsUrl + "?id=${light.id}",
                                        light, {}, {})
                                turnOffRequest.tag = this
                                requestQueue.add(turnOffRequest)
                            }
                        }

                    }, {
                        Log.e("Error", "Request failed")
                    }
                )

                val mediaRequest = StringRequest(
                    Request.Method.GET,
                    mediaUrl,
                    {
                        val gson = Gson()

                        mediaList = gson.fromJson<ArrayResult<mediaPlayer>>(it).result

                        for (media in mediaList){
                            if (media.isPlaying){
                                media.isPlaying = !media.isPlaying

                                val turnOffRequest =
                                    StringRequestWithBody(mediaUrl + "/pause?id=${media.id}&songId=${media.nowPlayingSongId}",
                                        media, {}, {})
                                turnOffRequest.tag = this
                                requestQueue.add(turnOffRequest)
                            }
                        }
                    },{
                        Log.e("Error", "Request failed")
                    }
                )

                doorRequest.tag = this
                lightRequest.tag = this
                mediaRequest.tag = this
                requestQueue.add(doorRequest)
                requestQueue.add(lightRequest)
                requestQueue.add(mediaRequest)

//                 TODO: create popup window that says garage doors have been close, media and lights turned
//                 off, and displays any non-motorized doors that are open
            }
        }
        else{
            binding.ConnectStatus.text = "Not connected, check internet access to continue"
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
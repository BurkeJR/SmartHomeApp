package com.example.smarthomeapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.smarthomeapp.databinding.FragmentLightsBinding
import com.google.gson.Gson

class LightsFragment : Fragment() {
    private lateinit var binding: FragmentLightsBinding
    private lateinit var adapter: LightsAdapter
    private lateinit var requestQueue: RequestQueue


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLightsBinding.inflate(layoutInflater)

        requestQueue = Volley.newRequestQueue(this.context)

        val ipAddress = getString(R.string.myIPAddress)
        val url = "http://$ipAddress/lights"
        lateinit var lightList: List<light>
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { thing ->
                val gson = Gson()

                lightList = gson.fromJson<ArrayResult<light>>(thing).result

                adapter = LightsAdapter()
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(context)
                adapter.submitList(lightList)

                adapter.onToggleLight = {
                    it.isOn = !it.isOn

                    val turnOnRequest = StringRequestWithBody(url + "?id=${it.id}", it, {}, {})
                    turnOnRequest.tag = this
                    requestQueue.add(turnOnRequest)
                }

            }, {
                Log.e("Error", "Request failed")
            }

        )
        stringRequest.tag = this

        requestQueue.add(stringRequest)

        return binding.root
    }
}
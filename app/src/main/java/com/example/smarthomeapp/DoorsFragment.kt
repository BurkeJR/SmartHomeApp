package com.example.smarthomeapp


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
import com.example.smarthomeapp.databinding.FragmentDoorsBinding
import com.google.gson.Gson


class DoorsFragment : Fragment() {
    private lateinit var binding: FragmentDoorsBinding
    private lateinit var adapter: DoorsAdapter
    private lateinit var requestQueue: RequestQueue


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDoorsBinding.inflate(inflater)


        requestQueue = Volley.newRequestQueue(this.context)
        //val url = "http://10.37.113.241/media-players" //Luke
        val url = "http://10.37.103.116/doors"  //Aaron
        lateinit var doorList: List<door>
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            {
                val gson = Gson()

                doorList = gson.fromJson<ArrayResult<door>>(it).result

                adapter = DoorsAdapter(doorList)
                binding.doorsListRecyclerView.adapter = adapter
                binding.doorsListRecyclerView.layoutManager = LinearLayoutManager(context)
                adapter.submitList(doorList)
                adapter.onItemClick = {
                    val action = DoorsFragmentDirections.actionDoorsFragmentToDoorsDetailsFragment()
                    findNavController().navigate(action)
                }
            },{
                Log.e("Error", "Request failed")
            }
        )

        stringRequest.tag = this
        requestQueue.add(stringRequest)

        return binding.root
    }

}
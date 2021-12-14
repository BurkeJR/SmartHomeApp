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
import com.example.smarthomeapp.databinding.FragmentMediaListBinding
import com.google.gson.Gson
import java.net.*

class MediaFragment : Fragment() {
    private lateinit var binding: FragmentMediaListBinding
    private lateinit var adapter: MediaAdapter
    private lateinit var requestQueue: RequestQueue


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaListBinding.inflate(inflater)


        requestQueue = Volley.newRequestQueue(this.context)

        val ipAddress = getString(R.string.myIPAddress)
        val url = "http://$ipAddress/media-players"
        lateinit var mediaList: List<mediaPlayer>
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            {
                val gson = Gson()

                mediaList = gson.fromJson<ArrayResult<mediaPlayer>>(it).result

                adapter = MediaAdapter(mediaList)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(context)
                adapter.submitList(mediaList)
                adapter.onItemClick = {
                    val action = MediaFragmentDirections.actionMediaFragmentToMediaDetailsFragment(it.name, it.isPlaying,it.id, it.nowPlayingSongId,it.currentTimeSeconds.toFloat())
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

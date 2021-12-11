package com.example.smarthomeapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.smarthomeapp.databinding.FragmentMediaDetailsBinding
import com.example.smarthomeapp.databinding.FragmentMediaListBinding
import com.google.gson.Gson


class MediaDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMediaDetailsBinding

    private lateinit var requestQueue: RequestQueue
    val args: MediaDetailsFragmentArgs by navArgs<MediaDetailsFragmentArgs>()
    var songID = -1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaDetailsBinding.inflate(inflater)
        //Set binding
        binding.mediaDetailTitleText.text = args.mediaName



        requestQueue = Volley.newRequestQueue(this.context)

        val ipAddress = getString(R.string.myIPAddress)
        val url = "http://$ipAddress/media-players/songs"

        var songList : List<song>

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            {
                val gson = Gson()

                songList = gson.fromJson<ArrayResult<song>>(it).result

                val coverUrl = songList[5].coverUrl

                Log.i("VOLLEY", "Songs loaded $coverUrl")
            },
            {
                // Handle error
                Log.e("VOLLEY", "Failed to load song list $it")
            }
        )
        stringRequest.tag = this

        requestQueue.add(stringRequest)

        binding.playMediaMusicButton.setOnClickListener {
            playMusic()
        }


        return binding.root
    }

    fun playMusic(){

        requestQueue = Volley.newRequestQueue(this.context)

        val ipAddress = getString(R.string.myIPAddress)
        val url = "http://$ipAddress/media-players"




        val stringRequest = StringRequest(
            Request.Method.POST,
            url,
            {
                val gson = Gson()

                val mediaList = gson.fromJson<ArrayResult<mediaPlayer>>(it).result

                mediaList[id].isPlaying = true
                mediaList[id].nowPlayingSongId = (songID + 1) % 5
                gson.toJson(mediaList)

            },
            {
                // Handle error
                Log.e("VOLLEY", "Failed to load song list $it")
            }
        )
        stringRequest.tag = this

        requestQueue.add(stringRequest)

    }

}
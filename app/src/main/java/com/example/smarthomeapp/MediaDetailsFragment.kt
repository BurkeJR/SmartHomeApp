package com.example.smarthomeapp

import android.media.MediaPlayer
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.learnsmarthome.StringRequestWithBody
import com.example.smarthomeapp.databinding.FragmentMediaDetailsBinding
import com.example.smarthomeapp.databinding.FragmentMediaListBinding
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject


class MediaDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMediaDetailsBinding

    private lateinit var requestQueue: RequestQueue
    val args: MediaDetailsFragmentArgs by navArgs<MediaDetailsFragmentArgs>()
    var songID = 0
    var playing = false
    lateinit var songList: List<song>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaDetailsBinding.inflate(inflater)
        //Set binding
        binding.mediaDetailTitleText.text = args.mediaName
        playing = args.isPlaying



        requestQueue = Volley.newRequestQueue(this.context)

        val ipAddress = getString(R.string.myIPAddress)
        val url = "http://$ipAddress/media-players/songs"


        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            {
                val gson = Gson()

                songList = gson.fromJson<ArrayResult<song>>(it).result

                val coverUrl = songList[5].coverUrl
                binding.songNameTextView.text = songList[songID].name

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
            if(playing){
                pauseMusic()
            }
            else {
                playMusic()
            }
        }
        binding.nextSongButton.setOnClickListener {
            nextSong()
        }


        return binding.root
    }

    fun playMusic() {
        data class MediaState(val isPlaying: Boolean)
        val turnOnMediaRequest = StringRequestWithBody("http://${getString(R.string.myIPAddress)}/media-players?id=${args.mediaID}", MediaState(true), {},{})

        turnOnMediaRequest.tag = this
        requestQueue.add(turnOnMediaRequest)
        binding.playMediaMusicButton.setImageResource(R.drawable.ic_baseline_pause_24)
        playing = true

    }

    fun pauseMusic(){
        data class MediaState(val isPlaying: Boolean)
        val turnOnMediaRequest = StringRequestWithBody("http://${getString(R.string.myIPAddress)}/media-players?id=${args.mediaID}", MediaState(false), {},{})

        turnOnMediaRequest.tag = this
        requestQueue.add(turnOnMediaRequest)
        binding.playMediaMusicButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        playing = false
    }
    fun nextSong(){
        songID = (songID + 1) % 5
        Log.i("MEDIA", "Song: $songID")
        data class MediaState(val nowPlayingSongId: Int, val currentTimeSeconds: Double)
        val changeSongRequest = StringRequestWithBody("http://${getString(R.string.myIPAddress)}/media-players?id=${args.mediaID}", MediaState(songID, 0.0), {}, {})
        changeSongRequest.tag = this
        requestQueue.add(changeSongRequest)
        binding.songNameTextView.text = songList[songID].name
    }

}
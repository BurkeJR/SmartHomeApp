package com.example.smarthomeapp

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.MediaPlayer
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.navArgs
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.learnsmarthome.StringRequestWithBody
import com.example.smarthomeapp.databinding.FragmentMediaDetailsBinding
import com.example.smarthomeapp.databinding.FragmentMediaListBinding
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import com.squareup.picasso.Picasso


class MediaDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMediaDetailsBinding

    private lateinit var requestQueue: RequestQueue
    val args: MediaDetailsFragmentArgs by navArgs<MediaDetailsFragmentArgs>()
    var songID = 0
    var numSongs = 1
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
        songID = args.songID
        if(playing){
            binding.playMediaMusicButton.setImageResource(R.drawable.ic_baseline_pause_24)
        }


        requestQueue = Volley.newRequestQueue(this.context)

        val ipAddress = getString(R.string.myIPAddress)
        val url = "http://$ipAddress/media-players/songs"


        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            {
                val gson = Gson()

                songList = gson.fromJson<ArrayResult<song>>(it).result


                binding.songNameTextView.text = songList[songID].name
                loadImage(songList[songID].coverUrl)
                numSongs = songList.size


                Log.i("VOLLEY", "Songs loaded")
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

        val turnOnMediaRequest = StringRequestWithBody("http://${getString(R.string.myIPAddress)}/media-players/play?id=${args.mediaID}&sondId=$songID", "", {},{})

        turnOnMediaRequest.tag = this
        requestQueue.add(turnOnMediaRequest)
        binding.playMediaMusicButton.setImageResource(R.drawable.ic_baseline_pause_24)
        playing = true

    }

    fun pauseMusic(){

        val turnOnMediaRequest = StringRequestWithBody("http://${getString(R.string.myIPAddress)}/media-players/pause?id=${args.mediaID}&songId=$songID", "", {},{})

        turnOnMediaRequest.tag = this
        requestQueue.add(turnOnMediaRequest)
        binding.playMediaMusicButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        playing = false
    }
    fun nextSong(){
        songID = (songID + 1) % numSongs
        Log.i("MEDIA", "Song: $songID")
        val changeSongRequest = StringRequestWithBody(
                "http://${getString(R.string.myIPAddress)}/media-players/play?id=${args.mediaID}&songId=$songID&currentTimeSeconds=0",
                "",
                {},
                {})
        changeSongRequest.tag = this
        requestQueue.add(changeSongRequest)

        if(!playing){
            playMusic()
        }

        loadImage(songList[songID].coverUrl)

        binding.songNameTextView.text = songList[songID].name
    }


    private fun loadImage(url: String) {
        Picasso.get().load(url).into(binding.albumCoverImage)

        val imgRequest = ImageRequest(
            url,
            {
                binding.albumCoverImage.setImageDrawable(BitmapDrawable(resources, it))
            },
            0, // Max width (or set to zero to use image’s normal height)
            0, // Max height (or set to zero to use image’s normal height)
            ImageView.ScaleType.CENTER_CROP, // How is the modified to fit into the max dimensions?
            Bitmap.Config.RGB_565,
            {
                Log.e("VOLLEY", "Image failed to load")
            }
        )

        imgRequest.tag = this

        requestQueue.add(imgRequest)
    }
}
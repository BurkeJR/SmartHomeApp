package com.example.smarthomeapp

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.navArgs
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.learnsmarthome.StringRequestWithBody
import com.example.smarthomeapp.databinding.FragmentMediaDetailsBinding
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class MediaDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMediaDetailsBinding

    private lateinit var requestQueue: RequestQueue
    val args: MediaDetailsFragmentArgs by navArgs<MediaDetailsFragmentArgs>()
    var songID = 0
    var numSongs = 1
    lateinit var bar: Thread
    private var currentTime = 0.0
    var playing = false
    private val handler = Handler()
    lateinit var songList: List<song>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaDetailsBinding.inflate(inflater)
        //Set binding
        binding.mediaDetailTitleText.text = args.mediaName
        playing = args.isPlaying
        currentTime = args.currentTimeSeconds.toDouble()
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
                binding.determinateBar.progress = ((currentTime/songList[songID].durationSeconds)*100).roundToInt()
                startProgressBar(args.currentTimeSeconds.toDouble(), songList[songID].durationSeconds)

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
        startProgressBar(currentTime, songList[songID].durationSeconds)

    }

    fun pauseMusic(){
        bar.interrupt()
        val turnOnMediaRequest = StringRequestWithBody("http://${getString(R.string.myIPAddress)}/media-players/pause?id=${args.mediaID}&songId=$songID", "", {},{})

        turnOnMediaRequest.tag = this
        requestQueue.add(turnOnMediaRequest)
        binding.playMediaMusicButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        playing = false
    }

    fun nextSong(){
        bar.interrupt()
        playing = false
        songID = (songID + 1) % numSongs
        Log.i("MEDIA", "Song: $songID")
        val changeSongRequest = StringRequestWithBody(
                "http://${getString(R.string.myIPAddress)}/media-players/play?id=${args.mediaID}&songId=$songID&currentTimeSeconds=0",
                "", {}, {})
        changeSongRequest.tag = this
        requestQueue.add(changeSongRequest)
        currentTime = 0.0
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

    private fun startProgressBar(time: Double, duration: Double){
        this.currentTime = time
        bar =Thread(Runnable {
            var interrupted = false
            while(binding.determinateBar.progress < 100 && !interrupted && playing){
                handler.post(Runnable {
                    binding.determinateBar.progress = ((currentTime/duration) * 100).roundToInt()
                    Log.i("Debug", "CurrentTime: $currentTime")
                })
                try {
                    currentTime += .1
                    Thread.sleep(100)
                }catch (e:InterruptedException){
                    e.printStackTrace()
                    interrupted = true
                }
            }
        })
        bar.start()
    }
}
package com.example.cataracta


//import android.R

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView


//todo add videoview
class MediaPlayerActivity : AppCompatActivity() {
    private lateinit var videoUri: Uri
    private var intent: Intent? = null
    private var path: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "mediaPlayer.onCreate()")
        // Set up the player instance
        setContentView(R.layout.activity_media_player)
        val player: ExoPlayer = ExoPlayer.Builder(this).build()
        try{
        val playerView : PlayerView = findViewById(R.id.playerView)
            if(playerView == null)
            Log.d(TAG, "null")
        }
        catch (e: Exception){
            Log.d(TAG, e.toString())
        }
//        val surface =  SurfaceView(this)
//
//        player.setVideoSurfaceView(surface)
//        playerView.player = player
        intent = getIntent()
        path = intent?.getStringExtra("path")
        Log.d(TAG, "mediaPlayer.onCreate()++")
        Log.d(TAG, "$path")
        videoUri = Uri.parse(path)
        // Build the media item.
        // val mediaItem: MediaItem = MediaItem.fromUri(uri)
        val mediaItem = MediaItem.Builder()
            .setUri(videoUri)
            .build()
        // Set the media item to be played.
        player.setMediaItem(mediaItem)
        // Prepare the player.
        player.prepare()
        // Start the playback.
        player.playWhenReady = true





}

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        // Set the MediaPlayer to play the video
        Log.d(TAG, "mediaPlayer.onstart()")
        Log.d(TAG, "$path")
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume(){
        super.onResume()

    }
    companion object {
        private const val TAG = "Cataracta/MediaPlayer"
    }

}

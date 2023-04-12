package com.example.cataracta


//import android.R

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView


@UnstableApi class MediaPlayerActivity : AppCompatActivity() {
    private lateinit var videoUri: Uri
    private var intent: Intent? = null
    private var path: String? = null
    private lateinit var player: ExoPlayer
    private lateinit var playerView : PlayerView
    private lateinit var mediaItem : MediaItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "mediaPlayer.onCreate()")
        // Set up the player instance
        setContentView(R.layout.activity_media_player)
        player = ExoPlayer.Builder(this).build()

        playerView=findViewById(R.id.player_view)
        // Bind the player to the view.
        playerView.player = player

        intent = getIntent()
        path = intent?.getStringExtra("path")
        Log.d(TAG, "mediaPlayer.onCreate()++")
        Log.d(TAG, "$path")
        videoUri = Uri.parse(path)
        mediaItem = MediaItem.fromUri(videoUri)

        // Instantiate the player.
        val player = ExoPlayer.Builder(this).build()
        // Attach player to the view.
        playerView.player = player
        playerView.controllerAutoShow = false
        player.setPlaybackSpeed(0.25F)
        // Set the media item to be played.
        player.setMediaItem(mediaItem)
        // Prepare the player.
        player.prepare()
        player.play()

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

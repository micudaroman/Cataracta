package com.example.cataracta


//import android.R
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import android.widget.VideoView;

//todo add videoview
class MediaPlayerActivity : AppCompatActivity() {
    lateinit var videoView: VideoView
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var videoUri: Uri
    private var intent: Intent? = null
//    getIntent()
    private var path: String? = null
    private var videoFile: File? = null


//    private var mediaPlayer: MediaPlayer? = null
//    private var videoView: VideoView? = null
    private var playButton: Button? = null
    private val pauseButton: Button? = null

//    var mediacontroller: MediaController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    Log.d(TAG, "mediaPlayer.onCreate()")
    setContentView(R.layout.activity_media_player)

    intent = getIntent()
    path = intent?.getStringExtra("path")
    Log.d(TAG, "mediaPlayer.onCreate()++")
    Log.d(TAG, "$path")
    videoFile = File(path)

    mediaPlayer = MediaPlayer.create(this, Uri.parse(videoFile?.absolutePath));
    videoView = findViewById(R.id.video_view);


    videoView.setVideoPath(videoFile!!.absolutePath)
//        videoView.setVideoURI(Uri.parse(videoFile!!.absolutePath));
    mediaPlayer?.start()

//    var playButton = findViewById<Button>(R.id.play_button)
//    playButton.setOnClickListener {
//        Log.d(TAG, "playButton++")
//        videoView.visibility = View.VISIBLE
//    }
//
//
//    var pauseButton = findViewById<Button>(R.id.pause_button);
//    pauseButton.setOnClickListener {
//        Log.d(TAG, "pauseButton++")
//        mediaPlayer?.pause();
//        videoView.setVisibility(View.INVISIBLE);
//    }

}

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onStart() {
        super.onStart()
        // Set the MediaPlayer to play the video
        Log.d(TAG, "mediaPlayer.onstart()")
        Log.d(TAG, "$path")
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onResume(){
        super.onResume()

    }
    companion object {
        private const val TAG = "Cataracta/MediaPlayer"
    }

}

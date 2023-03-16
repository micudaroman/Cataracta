package com.example.cataracta


import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MediaPlayerActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var playButton: Button? = null
    private var statusText: TextView? = null



    fun onClick(): View.OnClickListener? {
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer!!.pause()
            playButton?.setText("Play")
            statusText?.setText("Status: Paused")
        } else {
            mediaPlayer!!.start()
            playButton?.setText("Pause")
            statusText?.setText("Status: Playing")
        }
//        todo this is bullshit
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)
        mediaPlayer = MediaPlayer.create(this, R.raw.myvideofile)
        playButton = findViewById<Button>(R.id.play_button)
        statusText = findViewById<TextView>(R.id.status_text)

        playButton?.setOnClickListener( onClick() )


    }


    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }
}

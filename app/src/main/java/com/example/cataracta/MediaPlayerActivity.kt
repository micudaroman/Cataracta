package com.example.cataracta


//import android.R

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


@UnstableApi
class MediaPlayerActivity : AppCompatActivity() {
    private lateinit var videoUri: Uri
    private var path: String? = null
    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var mediaItem: MediaItem
    private lateinit var uploadButton: Button
    private val fileApi: FileApi = FileApi.invoke()

    private fun setupViews() {
        uploadButton = findViewById(R.id.my_image_button)
        uploadButton.setOnClickListener { uploadVideo() }
    }

    private fun setupPlayer() {
        path = intent?.getStringExtra("path")
        videoUri = Uri.parse(path)
        mediaItem = MediaItem.fromUri(videoUri)
        player = ExoPlayer.Builder(this).build()
        playerView = findViewById(R.id.player_view)
        playerView.player = player
        playerView.controllerAutoShow = false
        player.setPlaybackSpeed(0.25F)
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    private fun uploadVideo() {
        val videoPath = path?.substringAfterLast("/")
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
            "CameraX-Video/$videoPath"
        )
        if (file.exists()) {
            Log.d(ContentValues.TAG, "file exists")
        } else {
            Log.d(ContentValues.TAG, "error file doesnt exist")
            return
        }
        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)
        fileApi.uploadImage(filePart).enqueue(object : Callback<FileApi.UploadResponse> {
            override fun onResponse(
                call: Call<FileApi.UploadResponse>,
                response: Response<FileApi.UploadResponse>
            ) {
                if (response.isSuccessful) {
                    val uploadResponse = response.body()
                    if (uploadResponse != null) {
                        val leftEye = uploadResponse.left_eye
                        val rightEye = uploadResponse.right_eye
                        Log.d(
                            ContentValues.TAG,
                            "Upload successful. Left eye: $leftEye, Right eye: $rightEye"
                        )
                    } else {
                        Log.d(ContentValues.TAG, "Upload successful, but response body was null")
                    }
                } else {
                    Log.d(ContentValues.TAG, "Upload failed with error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FileApi.UploadResponse>, t: Throwable) {
                Log.d(ContentValues.TAG, "Upload failed with error: ${t.message}")
                val errorBody = t.message
                Log.d(ContentValues.TAG, "Error body: $errorBody")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)
        Log.d(TAG, "mediaPlayer.onCreate()")
        setupViews()
        setupPlayer()
    }

    override fun onStart() {
        super.onStart()
        // Set the MediaPlayer to play the video
        Log.d(TAG, "mediaPlayer.onstart()")
    }

    companion object {
        private const val TAG = "Cataracta/MediaPlayer"
    }

}

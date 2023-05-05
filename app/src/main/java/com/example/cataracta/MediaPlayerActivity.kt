package com.example.cataracta


//import android.R

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.ImageButton
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
    private var intent: Intent? = null
    private var path: String? = null
    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var mediaItem: MediaItem
    private var fileApi: FileApi = FileApi.invoke()
//    private val uploadButton = findViewById<ImageButton>(R.id.my_image_button)
    private lateinit var uploadButton: ImageButton // declare the variable here



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)
        uploadButton=findViewById<ImageButton>(R.id.my_image_button)
        Log.d(TAG, "mediaPlayer.onCreate()")


        val uri = Uri.parse("package:com.example.cataracta")
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted. Launch the part of the app that requires the permission.
            // For example, launch the code that reads or writes a file on external storage.
            // ...

        } else {

//            // Permission hasn't been granted yet. Request it.
            startActivity(
                Intent(
                    Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                    uri
                )
            )
        }


        uploadButton.setOnClickListener {
            val videoPath = path?.substringAfterLast("/")
            //movies directory + app directory + filename
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
                "CameraX-Video/$videoPath"
            )
            Log.d(ContentValues.TAG + "file canRead", file.canRead().toString())
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI

            if (file.exists()) {
                Log.d(ContentValues.TAG, "file exists")
            } else {
                Log.d(ContentValues.TAG, "error file doesnt exist")
            }
            val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)
//            LOGGING FILE CONTENT of request FILE
            Log.d(ContentValues.TAG + "requestBody.tostring", requestBody.toString())

//             Make the API call
            val call1: Call<FileApi.UploadResponse> = fileApi!!.uploadImage(filePart)
            call1.enqueue(object : Callback<FileApi.UploadResponse> {

                override fun onResponse(
                    call: Call<FileApi.UploadResponse>,
                    response: Response<FileApi.UploadResponse>
                ) {
                    Log.d(ContentValues.TAG, "Error body: ${response.message().toString()}")
                }

                override fun onFailure(call: Call<FileApi.UploadResponse>, t: Throwable) {
                    Log.d(ContentValues.TAG, "Upload failed with error: ${t.message}")
                    val errorBody = t.message
                    Log.d(ContentValues.TAG, "Error body: $errorBody")
                }
            })
        }


        // Set up the player instance
        player = ExoPlayer.Builder(this).build()

        playerView = findViewById(R.id.player_view)
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
//        player.play()

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

    override fun onResume() {
        super.onResume()

    }

    companion object {
        private const val TAG = "Cataracta/MediaPlayer"
    }

}

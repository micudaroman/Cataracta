package com.example.cataracta

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
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

@UnstableApi class mediaPlayerFragment: Fragment(R.layout.fragment_media_player) {
    private lateinit var videoUri: Uri
    private var path: String? = null
    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var mediaItem: MediaItem
    private lateinit var uploadButton: Button
    private val fileApi: FileApi = FileApi.invoke()


    private fun setupViews(view: View) {
        uploadButton = view.findViewById(R.id.my_image_button)
        uploadButton.setOnClickListener { uploadVideo() }
    }

    private fun setupPermissions() {
        val uri = Uri.parse("package:com.example.cataracta")
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))
        }
    }

    private fun setupPlayer() {
        path = requireArguments().getString("path")
        videoUri = Uri.parse(path)
        mediaItem = MediaItem.fromUri(videoUri)
        player = ExoPlayer.Builder(requireContext()).build()
        playerView = requireView().findViewById(R.id.player_view)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_media_player, container, false)
        Log.d(TAG, "mediaPlayer.onCreate()")
        setupViews(view)
        setupPermissions()
        setupPlayer()
        return view
    }

    override fun onResume() {
        super.onResume()
        // Set the MediaPlayer to play the video
        Log.d(TAG, "mediaPlayer.onResume()")
    }

    companion object {
        private const val TAG = "Cataracta/MediaPlayer"
    }

}
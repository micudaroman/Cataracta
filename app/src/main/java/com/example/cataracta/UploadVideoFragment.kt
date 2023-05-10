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
import androidx.media3.common.util.UnstableApi
import com.example.cataracta.databinding.FragmentUploadVideoBinding
import com.example.cataracta.ui.irisPreview.IrisPreviewFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

@UnstableApi class UploadVideoFragment: Fragment(R.layout.fragment_media_player) {
    private var path: String? = null
    private lateinit var uploadButton: Button
    private val fileApi: FileApi = FileApi.invoke()

    private lateinit var binding: FragmentUploadVideoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadVideoBinding.inflate(inflater)
        return binding.root

//        val view = inflater.inflate(R.layout.fragment_upload_video, container, false)
//        Log.d(TAG, "UploadVideo.onCreate()")
//        setupViews(view)
//        setupPermissions()
//        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    private fun setupViews(view: View) {
        path = requireArguments().getString("path")
        uploadButton = view.findViewById(R.id.upload_button)
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

                        val bundle = Bundle()
                        bundle.putString("leftEye", leftEye)
                        bundle.putString("rightEye", rightEye)
                        val fragment = IrisPreviewFragment()
                        fragment.arguments = bundle
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



    override fun onResume() {
        super.onResume()
        // Set the MediaPlayer to play the video
        Log.d(TAG, "UploadVideo.onResume()")
    }

    companion object {
        private const val TAG = "Cataracta/UploadVideo"
    }

}
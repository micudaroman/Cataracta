package com.example.cataracta.ui.record

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.camera.core.Camera
import androidx.camera.video.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.cataracta.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import android.content.pm.PackageManager
import android.widget.TextView
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.fragment.app.FragmentActivity
import com.example.cataracta.databinding.ActivityMainBinding
import com.example.cataracta.databinding.FragmentImageSelectionBinding
import java.util.concurrent.ExecutorService


class RecordFragment: Fragment() {
    var flashLightStatus: Boolean = false
//    abstract val contentResolver: ContentResolver
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private var camera: Camera? = null
//    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var lumens: TextView
    private var imageCapture: ImageCapture? = null

    private lateinit var binding:


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = RecordFragment.inflate(inflater)
        return inflater.inflate(R.layout.record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        viewBinding.videoCaptureButton.setOnClickListener { captureVideo() }

        cameraExecutor = Executors.newSingleThreadExecutor()

        viewBinding.infoButton.setOnClickListener {
            showPopup()
        }
        Toast.makeText(requireContext(),
            "Start camera",
            Toast.LENGTH_SHORT).show()


        startCamera()

        lumens = view.findViewById(R.id.lumen)

//        setupSensorStuff()
    }

//    private fun setupSensorStuff() {
//        sensorManager = getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
//        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
//    }

    companion object {
        private const val TAG = "Cataracta"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    private fun showPopup() {
//        val popupView = LayoutInflater.from(this).inflate(R.layout.info_popup_window, null)
//        val popupWindow = PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
//        popupWindow.isFocusable = true
//        popupWindow.showAsDropDown(viewBinding.popupHelper, 0, 0)
//
//        popupView.findViewById<View>(R.id.info_button_ok).setOnClickListener {
//            popupWindow.dismiss()
//        }
    }

    private fun captureVideo() {
        val videoCapture = this.videoCapture ?: return

        viewBinding.videoCaptureButton.isEnabled = false

        val curRecording = recording

        camera?.cameraControl?.enableTorch(true)

        if (curRecording != null) {
            // Stop the current recording session.
            curRecording.stop()
            camera?.cameraControl?.enableTorch(false)
            recording = null
            return
        }

        // Noise
        val toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 1000)
        toneGen.startTone(ToneGenerator.TONE_CDMA_MED_L, 500)

        // create and start a new recording session
        val name = SimpleDateFormat(RecordFragment.FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
            }
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()
        recording = videoCapture.output
            .prepareRecording(requireContext(), mediaStoreOutputOptions)
            .apply {
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED)
                {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(requireContext())) { recordEvent ->
                when(recordEvent) {
                    is VideoRecordEvent.Start -> {
                        viewBinding.videoCaptureButton.apply {
                            text = getString(R.string.stop_capture)
                            isEnabled = true
                        }
                    }
                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            val msg = "Video capture succeeded: " +
                                    "${recordEvent.outputResults.outputUri}"
//                            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT)
//                                .show()
                            Log.d(RecordFragment.TAG, msg)

// tomas
//                            intent = Intent(this, MediaPlayerActivity::class.java)
//                            intent.putExtra("path", "/storage/emulated/0/Movies/CameraX-Video/$name.mp4")
//                            startActivity(intent)\


                            Log.d(RecordFragment.TAG +"this file will be played", "/storage/emulated/0/Movies/CameraX-Video/$name.mp4")

                            val bundle = Bundle()
                            bundle.putString("path", "/storage/emulated/0/Movies/CameraX-Video/$name.mp4")
//                            val mediaPlayerFragment = MediaPlayerFragment()
//                            mediaPlayerFragment.arguments = bundle

                            //   tomas
//                            val transaction: FragmentTransaction =
//                                supportFragmentManager.beginTransaction()
//                            transaction.replace(R.id.container, mediaPlayerFragment)
//                            transaction.addToBackStack(null)
//                            transaction.commit()
//                            Log.d(TAG, "failure")


                        } else {
                            recording?.close()
                            recording = null
                            Log.e(
                                RecordFragment.TAG, "Video capture ends with error: " +
                                    "${recordEvent.error}")
                        }
                        viewBinding.videoCaptureButton.apply {
                            text = getString(R.string.start_capture)
                            isEnabled = true
                        }
                    }
                }
            }
    }
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            // ImageCapture
            imageCapture = ImageCapture.Builder()
                .build()

            // VideoCapture
            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.UHD))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, videoCapture)


            } catch(exc: Exception) {
                Log.e(RecordFragment.TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
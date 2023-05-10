package com.example.cataracta

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.cataracta.databinding.FragmentMediaPlayerBinding
import com.example.cataracta.ui.record.RecordFragment

class MediaPlayerFragment: Fragment() {
    private lateinit var videoUri: Uri
    private var path: String? = null
    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var mediaItem: MediaItem
    private lateinit var uploadButton: Button

    private lateinit var binding: FragmentMediaPlayerBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {;
        binding = FragmentMediaPlayerBinding.inflate(inflater, container, false)
        return binding.root
//        val view = inflater.inflate(R.layout.fragment_media_player, container, false)
//        Log.d(TAG, "mediaPlayer.onCreate()")
//        setupViews(view)
//        setupPlayer()
//        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.analysisButton.setOnClickListener{
            val args = Bundle()
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.my_fragment_container, UploadVideoFragment::class.java, args)
                .addToBackStack(MediaPlayerFragment::class.java.name)
                .commit()

        }
    }

    private fun setupViews(view: View?) {
        uploadButton = requireView().findViewById(R.id.analysis_button)
        uploadButton.setOnClickListener { uploadVideo() }
    }

    private fun setupPlayer() {
        path = requireArguments().getString("path")
        videoUri = Uri.parse(path)
        mediaItem = MediaItem.fromUri(videoUri)
        player = ExoPlayer.Builder(requireContext()).build()
        playerView = requireView().findViewById(R.id.player_view)
        playerView.player = player
//        playerView.controllerAutoShow = false
        player.setPlaybackSpeed(0.25F)
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    private fun uploadVideo() {



//        intent = Intent(this, MediaPlayerActivity::class.java)
//        intent.putExtra("path", "/storage/emulated/0/Movies/CameraX-Video/$name.mp4")
//        Log.d(MainActivity.TAG, "/storage/emulated/0/Movies/CameraX-Video/$name.mp4")
//        startActivity(intent)

        val bundle = Bundle()
        bundle.putString("path", "$path")
        val mediaPlayerFragment = UploadVideoFragment()
        mediaPlayerFragment.arguments = bundle
    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setupViews(view)
//        setupPlayer()
//    }

//    override fun  onViewCreated(view: View, savedInstanceState: Bundle?){
//        super.onViewCreated(view, savedInstanceState)
//        setupViews(view)
//        setupPlayer()
//    }

    override fun onStart() {
        super.onStart()
        setupPlayer()
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